package com.github.maxopoly.angeliacore.connection.login;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.angeliacore.SessionManager;
import com.github.maxopoly.angeliacore.libs.yaml.config.GlobalConfig;

public class AuthenticationHandler {

	private final static String authServerAdress = "https://authserver.mojang.com";
	private final static String sessionServerAdress = "https://sessionserver.mojang.com/session/minecraft/join";

	/**
	 * Creates the JSON payload to be sent to the API
	 * 
	 * @param userName    - The username of the player
	 * @param password    - The password of the player
	 * @param clientToken - The client token
	 * @return - The payload
	 */
	private static String constructAuthenticationJSON(String userName, String password, String clientToken) {
		JSONObject json1 = new JSONObject();
		json1.put("name", "Minecraft");
		json1.put("version", 1);
		JSONObject json = new JSONObject();
		json.put("agent", json1);
		json.put("username", userName);
		json.put("password", password);
		json.put("clientToken", clientToken);
		json.put("requestUser", true);
		return json.toString();
	}

	/**
	 * Creates a refresh JSON payload to be sent to Mojang
	 * 
	 * @param oldToken    - The old token
	 * @param playerUUID  - The UUID of the player
	 * @param playerName  - The name of the player
	 * @param clientToken - The client token
	 * @return - The payload
	 */
	private static String constructRefreshJSON(String oldToken, String playerUUID, String playerName,
			String clientToken) {
		JSONObject json = new JSONObject();
		json.put("accessToken", oldToken);
		json.put("clientToken", clientToken);
		return json.toString();
	}

	/**
	 * Creates a validation JSON payload to be sent to the API
	 * 
	 * @param accessToken - The access token
	 * @param clientToken - The client token
	 * @return - The payload
	 */
	private static String constructValidationJSON(String accessToken, String clientToken) {
		JSONObject json = new JSONObject();
		json.put("accessToken", accessToken);
		json.put("clientToken", clientToken);
		return json.toString();
	}

	private static long getRefreshDelay(Logger logger) {
		return new GlobalConfig(null, logger, new File("angeliaData/")).getTokenRefreshDelay();
	}

	private String accessToken;
	private String clientToken;
	private String playerName;
	private String playerUUID;
	private String email;

	private long lastRefresh;

	private SessionManager sessionManager;

	private String userID;

	private Logger logger;

	/**
	 * Authenticate with email and password to obtain auth token
	 * 
	 * @param sessionManager Manager overseeing this instance
	 * @param userName       User name of the player
	 * @param password       Password of the player
	 * @param clientToken    Client token to associate with the obtained auth token
	 * @param logger         Logger to use
	 * @throws IOException Thrown if no auth token could be obtained, either due to
	 *                     connection problems or due to auth problems
	 */
	public AuthenticationHandler(SessionManager sessionManager, String userName, String password, String clientToken,
			Logger logger) throws IOException {
		this.sessionManager = sessionManager;
		this.clientToken = clientToken;
		// we save the email, but the password is fire and forget
		this.email = userName;
		authenticate(userName, password, logger);
	}

	/**
	 * Reuse existing auth token
	 * 
	 * @param sessionManager Manager overseeing this instance
	 * @param userName       User name of the player
	 * @param accessToken    Secret auth token
	 * @param email          Email adress of the account
	 * @param playerUUID     UUID of the player without dashes
	 * @param userID         User id used internally
	 * @param clientToken    Client token tied to the auth token
	 * @param logger         Logger to use
	 * @param lastRefresh    When was the auth token last refreshed
	 */
	public AuthenticationHandler(SessionManager sessionManager, String userName, String accessToken, String email,
			String playerUUID, String userID, String clientToken, Logger logger, long lastRefresh) {
		this.sessionManager = sessionManager;
		this.playerName = userName;
		this.accessToken = accessToken;
		this.playerUUID = playerUUID;
		this.email = email;
		this.userID = userID;
		this.clientToken = clientToken;
		this.lastRefresh = lastRefresh;
		this.logger = logger;
	}

	/**
	 * Attempts validation
	 * 
	 * @throws IOException      - If something goes wrong
	 * @throws Auth403Exception - If you are being rate limited
	 */
	public void attemptValidation() throws IOException, Auth403Exception {
		try {
			if (!validateToken(logger)) {
				refreshToken(logger);
			}
		} catch (Auth403Exception e) {
			logger.warn("Auth for " + email + " could not be refreshed, deleting it");
			sessionManager.deleteAuth(this);
			throw e;
		}
	}

	/**
	 * Authenticates against the session server
	 * 
	 * @param sha    - The server id
	 * @param logger - The logger
	 * @throws IOException      - If something goes wrong
	 * @throws Auth403Exception - If rate limit occurs
	 */
	public void authAgainstSessionServer(String sha, Logger logger) throws IOException, Auth403Exception {
		if (accessToken == null || playerUUID == null) {
			throw new IOException("Access token isn't available yet");
		}
		JSONObject json = new JSONObject();
		json.put("accessToken", accessToken);
		json.put("selectedProfile", playerUUID);
		json.put("serverId", sha);
		try {
			sendPost(json.toString(), sessionServerAdress, logger);
		} catch (Auth403Exception e) {
			sessionManager.deleteAuth(this);
			throw e;
		}
	}

	/**
	 * Attempts authentication
	 * 
	 * @param username - Username of the player
	 * @param password - Password of the player
	 * @param logger   - The logger
	 * @return Whether the account authenticated with success
	 * @throws IOException - If anything goes wrong
	 */
	private boolean authenticate(String username, String password, Logger logger) throws IOException {
		this.lastRefresh = System.currentTimeMillis();
		String result;
		try {
			result = sendPost(constructAuthenticationJSON(username, password, clientToken),
					authServerAdress + "/authenticate", logger);
		} catch (Auth403Exception e) {
			logger.info(
					"Failed to auth account " + username + ". Either you are rate limited or the auth server is down");
			return false;
		}
		JSONObject jsonResult = new JSONObject(result);
		accessToken = jsonResult.getString("accessToken");
		String receivedClientToken = jsonResult.getString("clientToken");
		if (!clientToken.equals(receivedClientToken)) {
			throw new IOException("Received different client token during auth");
		}
		JSONObject selectedProfile = jsonResult.getJSONObject("selectedProfile");
		playerName = selectedProfile.getString("name");
		playerUUID = selectedProfile.getString("id");
		userID = jsonResult.getJSONObject("user").getString("id");
		logger.info("Successfully authenticated " + playerName + " with UUID " + playerUUID);
		sessionManager.updateAuth(this);
		return true;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getClientToken() {
		return clientToken;
	}

	public String getEmail() {
		return email;
	}

	public long getLastTokenRefresh() {
		return lastRefresh;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getPlayerUUID() {
		return playerUUID;
	}

	public String getUserID() {
		return userID;
	}

	/**
	 * Attempts to refresh the current access token. Note that even though a token
	 * might no longer be valid, it may still be possible to refresh it
	 *
	 * @param logger Logger used
	 * @throws IOException      In case of connection problems
	 * @throws Auth403Exception In case the auth server rejects, likely due to rate
	 *                          limiting
	 */
	private void refreshToken(Logger logger) throws IOException, Auth403Exception {
		String result;
		result = sendPost(constructRefreshJSON(accessToken, playerUUID, playerName, clientToken),
				authServerAdress + "/refresh", logger);
		JSONObject jsonResult = new JSONObject(result);
		accessToken = jsonResult.getString("accessToken");
		String receivedClientToken = jsonResult.getString("clientToken");
		if (!clientToken.equals(receivedClientToken)) {
			throw new IOException("Received different client token during access token refresh");
		}
		logger.info("Successfully refreshed access token for " + playerName);
		this.lastRefresh = System.currentTimeMillis();
		sessionManager.updateAuth(this);
	}

	/**
	 * Sends a post request to the specified URL
	 * 
	 * @param content - The content of the body
	 * @param url     - The URL of the payload
	 * @param logger  - The logger where to output this
	 * @return The reponse
	 * @throws IOException      - If a non 2XX reponse code is received
	 * @throws Auth403Exception - If 403 (rate limit) occurs
	 */
	private String sendPost(String content, String url, Logger logger) throws IOException, Auth403Exception {
		byte[] contentBytes = content.getBytes("UTF-8");
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(contentBytes, 0, contentBytes.length);
		wr.close();
		int responseCode = con.getResponseCode();
		if ((responseCode / 100) != 2) { // we want a 200 something response code
			if (responseCode == 403) {
				throw new Auth403Exception("Auth server rejected auth attempt for account " + email
						+ ". Either you are rate limited or the auth server is down");
			} else {
				throw new IOException("POST to " + url + " returned bad response code " + responseCode);
			}
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	/**
	 * Checks if the given token is valid
	 * 
	 * @param - The logger
	 * @return The information requested
	 * @throws IOException If anything goes wrong
	 */
	public boolean validateToken(Logger logger) throws IOException {
		if (getRefreshDelay(logger) > (System.currentTimeMillis() - lastRefresh)) {
			// assume valid
			return true;
		}
		try {
			sendPost(constructValidationJSON(accessToken, clientToken), authServerAdress + "/validate", logger);
			this.lastRefresh = System.currentTimeMillis();
		} catch (Auth403Exception e) {
			return false;
		}
		// we dont have an actual response in this case, just a 204 response
		// Interesting enough it is impossible to tell being rate limited apart from an
		// invalid token just based on the
		// validation attempt
		return true;
	}

}
