package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.util.ChatParser;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class ChatMessagePacketHandler extends AbstractIncomingPacketHandler {

	public ChatMessagePacketHandler(ServerConnection connection) {
		super(connection, 0x0F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			String jsonChat = packet.readString();
			System.out.println("CHAT: " + ChatParser.getRawText(jsonChat));
			byte position = packet.readUnsignedByte();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse chat packet", e);
		}

	}

}
