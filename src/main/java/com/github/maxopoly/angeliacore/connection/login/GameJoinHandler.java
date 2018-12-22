package com.github.maxopoly.angeliacore.connection.login;

import java.io.IOException;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.compression.MalformedCompressedDataException;
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

	public void parseLoginSuccess() throws IOException {
		while (true) {
			ReadOnlyPacket loginPacket;
			try {
				loginPacket = connection.getPacket();
			} catch (MalformedCompressedDataException e) {
				throw new IOException("Compression of login success packet was invalid");
			}
			if (loginPacket.getPacketID() == 3) {
				handleCompressionPacket(loginPacket);
			} else if (loginPacket.getPacketID() == 2) {
				handleLoginSuccess(loginPacket);
				return;
			} else if (loginPacket.getPacketID() == 0) {
				handleDisconnectPacket(loginPacket);
				return;
			} else
				throw new IOException("Received invalid packet with id " + loginPacket.getPacketID()
						+ " while waiting for login success");
		}
	}
}
