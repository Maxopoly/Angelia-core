package com.github.maxopoly.angeliacore.connection.login;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class AuthenticationHandler {

	private final static String authServerAdress = "https://authserver.mojang.com";
	private final static String sessionServerAdress = "https://sessionserver.mojang.com/session/minecraft/join";
	private final static String separator = ";;;;";

	private String accessToken;
	private String clientToken;
	private String playerName;
	private String playerID;
	private SecureRandom random = new SecureRandom();

	public AuthenticationHandler(String userName, String password, Logger logger) throws IOException {
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
	public AuthenticationHandler(String serialized) throws IOException {
		if (serialized == null) {
			throw new IOException("Can't deserialize null?");
		}
		String[] split = serialized.split(separator);
		if (split.length != 4) {
			throw new IOException("Invalid serialization format");
		}
		this.accessToken = split[0];
		this.clientToken = split[1];
		this.playerName = split[2];
		this.playerID = split[3];
	}

	/**
	 * Serializes this instances accessToken, clientToken, playerName and playerID so they can be used across sessions
	 * 
	 * @return Serialization of this instance
	 */
	public String serialize() {
		return accessToken + separator + clientToken + separator + playerName + separator + playerID;
	}

	public void authenticate(String userName, String password, Logger logger) throws IOException {
		clientToken = new BigInteger(130, random).toString(32);
		String result = sendPost(constructAuthenticationJSON(userName, password, clientToken), authServerAdress
				+ "/authenticate", logger);
		JSONObject jsonResult = new JSONObject(result);
		accessToken = jsonResult.getString("accessToken");
		String receivedClientToken = jsonResult.getString("clientToken");
		if (!clientToken.equals(receivedClientToken)) {
			throw new IOException("Received different client token during auth");
		}
		JSONObject selectedProfile = jsonResult.getJSONObject("selectedProfile");
		playerName = selectedProfile.getString("name");
		playerID = selectedProfile.getString("id");
		logger.info("Successfully authenticated " + playerName + " with UUID " + playerID);
	}

	/**
	 * Attempts to refresh the current access token. Note that even though a token might no longer be valid, it may still
	 * be possible to refresh it
	 * 
	 * @param logger
	 *          Logger used
	 * @throws IOException
	 */
	public void refreshToken(Logger logger) throws IOException {
		if (accessToken == null || playerID == null || playerName == null || clientToken == null) {
			throw new IOException("Can not refresh auth token with missing auth information");
		}
		String result = sendPost(constructRefreshJSON(accessToken, playerID, playerName, clientToken), authServerAdress
				+ "/refresh", logger);
		JSONObject jsonResult = new JSONObject(result);
		accessToken = jsonResult.getString("accessToken");
		String receivedClientToken = jsonResult.getString("clientToken");
		if (!clientToken.equals(receivedClientToken)) {
			throw new IOException("Received different client token during access token refresh");
		}
		logger.info("Successfully refreshed access token for " + playerName);
	}

	public boolean validateToken(Logger logger) throws IOException {
		try {
			sendPost(constructValidationJSON(accessToken, clientToken), authServerAdress + "/validate", logger);
		} catch (IOException e) {
			if (e.getMessage().startsWith("POST to")) {
				// 403 response code
				return false;
			} else {
				throw e;
			}
		}
		// we dont have an actual response in this case, just a 204 response
		return true;
	}

	public void authAgainstSessionServer(String sha, Logger logger) throws IOException {
		if (accessToken == null || playerID == null) {
			throw new IOException("Access token isn't available yet");
		}
		JSONObject json = new JSONObject();
		json.put("accessToken", accessToken);
		json.put("selectedProfile", playerID);
		json.put("serverId", sha);
		sendPost(json.toString(), sessionServerAdress, logger);
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

	public String getPlayerID() {
		return playerID;
	}

	private String sendPost(String content, String url, Logger logger) throws IOException {
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
				throw new IOException("POST to " + url + " returned bad response code " + responseCode);
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

}
