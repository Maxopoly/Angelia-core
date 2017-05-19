package com.github.maxopoly.angeliacore.connection.login;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
	private String playerID;

	public void authenticate(String userName, String password, Logger logger) throws IOException {
		String result = sendPost(constructAuthenticationJSON(userName, password), authServerAdress + "/authenticate",
				logger);
		JSONObject jsonResult = new JSONObject(result);
		accessToken = jsonResult.getString("accessToken");
		clientToken = jsonResult.getString("clientToken");
		JSONObject selectedProfile = jsonResult.getJSONObject("selectedProfile");
		playerName = selectedProfile.getString("name");
		playerID = selectedProfile.getString("id");

	}

	public void authAgainstSessionServer(String sha, Logger logger) throws IOException {
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

	private static String constructAuthenticationJSON(String userName, String password) {
		JSONObject json1 = new JSONObject();
		json1.put("name", "Minecraft");
		json1.put("version", 1);
		JSONObject json = new JSONObject();
		json.put("agent", json1);
		json.put("username", userName);
		json.put("password", password);
		return json.toString();
	}

}
