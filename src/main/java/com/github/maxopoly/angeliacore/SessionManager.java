package com.github.maxopoly.angeliacore;

import com.github.maxopoly.angeliacore.connection.login.AuthenticationHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class SessionManager {

	// we dont want people to ever touch this file manually, so we dont make it a .txt
	private String saveFile = "tokens.ang";
	private Logger logger;

	private Map<String, AuthenticationHandler> auths;

	public SessionManager(Logger logger) {
		auths = new HashMap<String, AuthenticationHandler>();
		this.logger = logger;
		loadTokens();
	}

	public AuthenticationHandler getAccount(String accountName) {
		return auths.get(accountName);
	}

	public AuthenticationHandler authNewAccount(String userName, String password) {
		AuthenticationHandler authHandler;
		try {
			authHandler = new AuthenticationHandler(userName, password, logger);
		} catch (IOException e) {
			logger.error("Failed to auth account " + userName, e);
			return null;
		}
		return authHandler;
	}

	public List<String> getAvailableAccounts() {
		return new LinkedList<String>(auths.keySet());
	}

	public void saveTokens() {
		if (auths.size() == 0) {
			return;
		}
		File f = FileSystems.getDefault().getPath(saveFile).toFile();
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				logger.error("Failed to create save file for access tokens", e);
				return;
			}
		}
		try (PrintWriter out = new PrintWriter(new FileOutputStream(f))) {
			for (AuthenticationHandler authHandler : auths.values()) {
				out.println(authHandler.serialize());
			}
		} catch (FileNotFoundException e) {
			logger.error("Could not find access token save file?", e);
		}
	}

	private void loadTokens() {
		File f = new File(System.getProperty("user.dir"));
		if (!f.exists()) {
			logger.info("No access tokens were loaded from save file as safe file didn't exist");
			return;
		}
		try {
			// praise progress in java for making this so easy
			for (String line : Files.readAllLines(FileSystems.getDefault().getPath(saveFile))) {
				if (line.equals("")) {
					continue;
				}
				AuthenticationHandler authHandler;
				try {
					authHandler = new AuthenticationHandler(line);
				} catch (IOException e) {
					logger.error("Failed to parse authHandler from line " + line, e);
					continue;
				}
				if (!authHandler.validateToken(logger)) {
					try {
						authHandler.refreshToken(logger);
					} catch (IOException e) {
						logger.error("Failed to refresh token for " + authHandler.getPlayerName());
						continue;
					}
				}
				logger.info("Successfully loaded authentication for " + authHandler.getPlayerName());
				auths.put(authHandler.getPlayerName(), authHandler);
			}
		} catch (IOException e) {
			logger.error("Failed to parse token file", e);
		}
	}
}
