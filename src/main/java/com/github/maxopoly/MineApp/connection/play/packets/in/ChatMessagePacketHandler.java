package com.github.maxopoly.MineApp.connection.play.packets.in;

import com.github.maxopoly.MineApp.ChatParser;
import com.github.maxopoly.MineApp.connection.ServerConnection;
import com.github.maxopoly.MineApp.packet.EndOfPacketException;
import com.github.maxopoly.MineApp.packet.ReadOnlyPacket;

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
