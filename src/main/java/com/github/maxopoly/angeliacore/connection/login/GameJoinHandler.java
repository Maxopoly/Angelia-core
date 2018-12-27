package com.github.maxopoly.angeliacore.connection.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.angeliacore.connection.DisconnectReason;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.compression.MalformedCompressedDataException;
import com.github.maxopoly.angeliacore.connection.encryption.AES_CFB8_Encrypter;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class GameJoinHandler {

	private ServerConnection connection;

	public GameJoinHandler(ServerConnection connection) {
		this.connection = connection;
	}

	public void handleCompressionPacket(ReadOnlyPacket packet) {
		int maximumSize = packet.readVarInt();
		if (maximumSize > 0) {
			connection.activateCompression(maximumSize);
		}
	}

	public void handleDisconnectPacket(ReadOnlyPacket loginPacket) {
		String msg = null;
		try {
			msg = loginPacket.readString();
			connection.getLogger().error("Server disconnected with reason: " + msg);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse disconnect package", e);
		}
	}

	public void handleLoginSuccess(ReadOnlyPacket packet) throws IOException {
		String playerUUID;
		String userName;
		try {
			playerUUID = packet.readString();
			userName = packet.readString();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Received invalid login success package");
		}
		connection.getLogger()
				.info("Received login success package for player " + userName + " with UUID " + playerUUID);
	}

	public void parseGameJoin(Logger logger, String serverAddress) throws IOException, Auth403Exception {
		while (true) {
			ReadOnlyPacket gameJoinPacket;
			try {
				gameJoinPacket = connection.getPacket();
			} catch (MalformedCompressedDataException e) {
				throw new IOException("Compression of login success packet was invalid");
			}
			if (gameJoinPacket.getPacketID() == 3) {
				handleCompressionPacket(gameJoinPacket);
			} else if (gameJoinPacket.getPacketID() == 2) {
				handleLoginSuccess(gameJoinPacket);
				return;
			} else if(gameJoinPacket.getPacketID() == 1) {
				// figure out encryption secret
				logger.info("Parsing encryption request from " + serverAddress);
				EncryptionHandler asyncEncHandler = new EncryptionHandler(connection);
				if (!asyncEncHandler.parseEncryptionRequest(gameJoinPacket)) {
					logger.info("Failed to handle encryption request, could not setup connection");
					connection.close(DisconnectReason.Unknown_Connection_Error);
					return;
				}
				asyncEncHandler.genSecretKey();
				logger.info("Authenticating connection attempt to " + serverAddress + " against Yggdrassil session server");
				connection.authHandler.authAgainstSessionServer(asyncEncHandler.generateKeyHash(), logger);
				logger.info("Sending encryption reply to " + serverAddress);
				asyncEncHandler.sendEncryptionResponse();
				// everything from here on is encrypted
				logger.info("Enabling sync encryption with " + serverAddress);
				connection.encryptionEnabled = true;
				connection.syncEncryptionHandler = new AES_CFB8_Encrypter(asyncEncHandler.getSharedSecret(),
						asyncEncHandler.getSharedSecret());
			} else if (gameJoinPacket.getPacketID() == 0) {
				handleDisconnectPacket(gameJoinPacket);
				return;
			} else
				throw new IOException("Received invalid packet with id " + gameJoinPacket.getPacketID()
						+ " while waiting for login success");
		}
	}
}
