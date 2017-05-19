package com.github.maxopoly.MineApp.connection.login;

import com.github.maxopoly.MineApp.connection.ServerConnection;
import com.github.maxopoly.MineApp.packet.EndOfPacketException;
import com.github.maxopoly.MineApp.packet.ReadOnlyPacket;
import java.io.IOException;

public class GameJoinHandler {

	private ServerConnection connection;

	public GameJoinHandler(ServerConnection connection) {
		this.connection = connection;
	}

	public void parseLoginSuccess() throws IOException {
		while (true) {
			ReadOnlyPacket loginPacket = connection.getPacket();
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

	private void handleDisconnectPacket(ReadOnlyPacket loginPacket) throws IOException {
		String msg;
		try {
			msg = loginPacket.readString();
			connection.getLogger().error("Server disconnected with reason: " + msg);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse disconnect package", e);
		}
		throw new IOException("Server disconnected");

	}

	public void handleCompressionPacket(ReadOnlyPacket packet) {
		int maximumSize = packet.readVarInt();
		if (maximumSize > 0) {
			connection.activateCompression(maximumSize);
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
		connection.getLogger().info("Received login success package for player " + userName + " with UUID " + playerUUID);
	}
}
