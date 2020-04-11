package com.github.maxopoly.angeliacore.connection.login;

import java.io.IOException;

import org.json.JSONObject;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class HandShake {

	private ServerConnection connection;

	public HandShake(ServerConnection connection) {
		this.connection = connection;
	}

	/**
	 * Creates a handshake message for the server
	 * 
	 * @param host            - The host of the server
	 * @param port            - The port of the server
	 * @param beginLogin      - If login has begun
	 * @param protocolVersion - The protocol version
	 * @return The generated packet
	 * @throws IOException - If anything goes wrong
	 */
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

	/**
	 * Gets the protocol version from the server
	 * 
	 * @return The protocol version read
	 * @throws IOException If anything goes wrong
	 */
	public int requestProtocolVersion() throws IOException {
		String json = send(false, -1);
		JSONObject jsonObject = new JSONObject(json);
		JSONObject versionObject = jsonObject.getJSONObject("version");
		int protocolNumber = versionObject.getInt("protocol");
		String versionName = versionObject.getString("name");
		connection.getLogger()
				.info(connection.getAdress() + " is on version " + versionName + " with protocol " + protocolNumber);
		return protocolNumber;
	}

	/**
	 * Executes the entire handshake process
	 * 
	 * @param requestConnection - Whether to request a connection
	 * @param protocolVersion   - The protocol version
	 * @return The response to the handshake
	 * @throws IOException - If anything goes wrong
	 */
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
				// The suppress warnings here is justified, since despite not needing the value,
				// we still need to read it from the packet.
				@SuppressWarnings("unused")
				long pingtime = pongPacket.readLong(); // read response
			}
		} catch (Exception e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Failed to handshake with server " + " - " + e.getClass());
		}
		return json;
	}

	/**
	 * Sends a login start message for a specific player
	 * 
	 * @param playerName - The name of the player
	 * @throws IOException - If anything goes wrong.
	 */
	public void sendLoginStartMessage(String playerName) throws IOException {
		WriteOnlyPacket loginStart = new WriteOnlyPacket(0x00);
		loginStart.writeString(playerName);
		connection.sendPacket(loginStart);
	}
}
