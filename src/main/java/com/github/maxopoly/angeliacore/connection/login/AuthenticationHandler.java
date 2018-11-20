package com.github.maxopoly.angeliacore.connection.login;

import com.github.maxopoly.angeliacore.SessionManager;
import com.github.maxopoly.angeliacore.config.GlobalConfig;
import com.github.maxopoly.angeliacore.exceptions.Auth403Exception;
import com.github.maxopoly.angeliacore.exceptions.OutDatedAuthException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class AuthenticationHandler {

	private final static String authServerAdress = "https://authserver.mojang.com";
	private final static String sessionServerAdress = "https://sessionserver.mojang.com/session/minecraft/join";

	private String accessToken;
	private String clientToken;
	private String playerName;
	private String playerUUID;
	private String email;
	private long lastRefresh;
	private SessionManager sessionManager;
	private String userID;

	public AuthenticationHandler(SessionManager sessionManager, String userName, String password, String clientToken,
			Logger logger) throws IOException {
		this.sessionManager = sessionManager;
		this.clientToken = clientToken;
		// we save the email, but the password is fire and forget
		this.email = userName;
		authenticate(userName, password, logger);
	}

	/**
	 * Constructs an instance from a serialize line containing an accesstoken, client token, player name and player uuid
	 * which all belong together.
	 *
	 * @param serialized
	 * @throws IOException
	 *           If the String wasnt properly formatted
	 */
	public AuthenticationHandler(SessionManager sessionManager, String userName, String accessToken, String email,
			String playerUUID, String userID, String clientToken, Logger logger, long lastRefresh) throws IOException, OutDatedAuthException {
		this.sessionManager = sessionManager;
		this.playerName = userName;
		this.accessToken = accessToken;
		this.playerUUID = playerUUID;
		this.email = email;
		this.userID = userID;
		this.clientToken = clientToken;
		this.lastRefresh = lastRefresh;
		if (!validateToken(logger)) {
			if (!refreshToken(logger)) {
				throw new OutDatedAuthException("Could not refresh token");
			}
		}
	}

	private boolean authenticate(String userName, String password, Logger logger) throws IOException {
		this.lastRefresh = System.currentTimeMillis();
		String result;
		try {
			result = sendPost(constructAuthenticationJSON(userName, password, clientToken), authServerAdress
					+ "/authenticate", logger);
		} catch (Auth403Exception e) {
			logger.info("Failed to auth account " + userName + ". Either you are rate limited or the auth server is down");
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

	/**
	 * Attempts to refresh the current access token. Note that even though a token might no longer be valid, it may still
	 * be possible to refresh it
	 *
	 * @param logger
	 *          Logger used
	 * @throws IOException
	 */
	public boolean refreshToken(Logger logger) throws IOException {
		if (getRefreshDelay(logger) > (System.currentTimeMillis() - lastRefresh)) {
			//assume valid
			return true;
		}
		String result;
		try {
			result = sendPost(constructRefreshJSON(accessToken, playerUUID, playerName, clientToken), authServerAdress
					+ "/refresh", logger);
		} catch (Auth403Exception e) {
			logger.info("Failed to refresh token for " + email);
			sessionManager.deleteAuth(this);
			return false;
		}
		JSONObject jsonResult = new JSONObject(result);
		accessToken = jsonResult.getString("accessToken");
		String receivedClientToken = jsonResult.getString("clientToken");
		if (!clientToken.equals(receivedClientToken)) {
			throw new IOException("Received different client token during access token refresh");
		}
		logger.info("Successfully refreshed access token for " + playerName);
		this.lastRefresh = System.currentTimeMillis();
		sessionManager.updateAuth(this);
		return true;
	}

	public boolean validateToken(Logger logger) throws IOException {
		if (getRefreshDelay(logger) > (System.currentTimeMillis() - lastRefresh)) {
			//assume valid
			return true;
		}
		try {
			sendPost(constructValidationJSON(accessToken, clientToken), authServerAdress + "/validate", logger);
			this.lastRefresh = System.currentTimeMillis();
		} catch (Auth403Exception e) {
			return false;
		}
		// we dont have an actual response in this case, just a 204 response
		// Interesting enough it is impossible to tell being rate limited apart from an invalid token just based on the
		// validation attempt
		return true;
	}

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

	public String getAccessToken() {
		return accessToken;
	}

	public String getClientToken() {
		return clientToken;
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

	public String getEmail() {
		return email;
	}
	
	public long getLastTokenRefresh() {
		return lastRefresh;
	}

	private String sendPost(String content, String url, Logger logger) throws IOException, Auth403Exception {
		try {
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
		} catch (Exception e) {
			logger.error("Exception occured", e);
			throw new IOException("Failed to send POST to " + url + " : " + e.getClass());
		}
	}

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

	private static String constructRefreshJSON(String oldToken, String playerUUID, String playerName, String clientToken) {
		JSONObject json1 = new JSONObject();
		json1.put("id", playerUUID);
		json1.put("name", playerName);
		JSONObject json = new JSONObject();
		json.put("selectedProfile", json1);
		json.put("accessToken", oldToken);
		json.put("clientToken", clientToken);
		return json.toString();
	}

	private static String constructValidationJSON(String accessToken, String clientToken) {
		JSONObject json = new JSONObject();
		json.put("accessToken", accessToken);
		json.put("clientToken", clientToken);
		return json.toString();
	}
	
	private static long getRefreshDelay(Logger logger) {
		return new GlobalConfig(null, logger, new File ("angeliaData/")).getTokenRefreshDelay();
	}

}
