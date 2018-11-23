package com.github.maxopoly.angeliacore.connection.login;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

import java.io.IOException;
import org.json.JSONObject;

public class HandShake {

	private ServerConnection connection;

	public HandShake(ServerConnection connection) {
		this.connection = connection;
	}

	public String send(boolean requestConnection, int protocolVersion) throws IOException {
		String json = null;
		try {
			// C->S : Handshake
			WriteOnlyPacket handshakePacket = createHandshakeMessage(connection.getAdress(), connection.getPort(),
					requestConnection, protocolVersion);
			connection.sendPacket(handshakePacket);
			if (!requestConnection) {
				// C->S : Request
				WriteOnlyPacket requestPacket = new WriteOnlyPacket(0x00);
				connection.sendPacket(requestPacket);

				// S->C : Response
				ReadOnlyPacket responsePacket = connection.getPacket();
				if (responsePacket.getPacketID() != 0x00) { // we want a status response
					throw new IOException("Invalid packetID in server response packet");
				}
				json = responsePacket.readString();
				// remove leading spaces etc.
				json = json.substring(json.indexOf('{'), json.length());
				// C->S : Ping
				long now = System.currentTimeMillis();
				WriteOnlyPacket timePingPacket = new WriteOnlyPacket(0x01);
				timePingPacket.writeLong(now);
				connection.sendPacket(timePingPacket);

				// S->C : Pong
				ReadOnlyPacket pongPacket = connection.getPacket();
				int packetId = pongPacket.getPacketID();
				if (packetId != 0x01) {
					throw new IOException("Invalid packetID in server pong packet");
				}
				long pingtime = pongPacket.readLong(); // read response
				// Main.logger.info("Received handshake reply: " + json);
			}
		} catch (Exception e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Failed to handshake with server " + " - " + e.getClass());
		}
		return json;
	}

	public int requestProtocolVersion() throws IOException {
		String json = send(false, -1);
		JSONObject jsonObject = new JSONObject(json);
		JSONObject versionObject = jsonObject.getJSONObject("version");
		int protocolNumber = versionObject.getInt("protocol");
		String versionName = versionObject.getString("name");
		connection.getLogger().info(
				connection.getAdress() + " is on version " + versionName + " with protocol " + protocolNumber);
		return protocolNumber;
	}

	private WriteOnlyPacket createHandshakeMessage(String host, int port, boolean beginLogin, int protocolVersion)
			throws IOException {
		WriteOnlyPacket handshakePacket = new WriteOnlyPacket(0x00);
		handshakePacket.writeVarInt(protocolVersion);
		handshakePacket.writeString(host);
		handshakePacket.writeShort((short) port);
		int status = beginLogin ? 2 : 1;
		handshakePacket.writeVarInt(status);
		return handshakePacket;
	}

	public void sendLoginStartMessage(String playerName) throws IOException {
		WriteOnlyPacket loginStart = new WriteOnlyPacket(0x00);
		loginStart.writeString(playerName);
		connection.sendPacket(loginStart);
	}
}
