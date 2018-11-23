package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.ChatMessageReceivedEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.util.ChatParser;

public class ChatMessagePacketHandler extends AbstractIncomingPacketHandler {

	public ChatMessagePacketHandler(ServerConnection connection) {
		super(connection, 0x0F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			String jsonChat = packet.readString();
			ChatMessageReceivedEvent event = new ChatMessageReceivedEvent(jsonChat, ChatParser.getRawText(jsonChat));
			connection.getEventHandler().broadcast(event);
			byte position = packet.readByte();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse chat packet", e);
		}

	}

}
