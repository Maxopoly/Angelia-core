package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.player.ChatMessageReceivedEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.chat.ChatComponentParser;

public class ChatMessagePacketHandler extends AbstractIncomingPacketHandler {

	public ChatMessagePacketHandler(ServerConnection connection) {
		super(connection, 0x0F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			String jsonChat = packet.readString();
			ChatMessageReceivedEvent event = new ChatMessageReceivedEvent(jsonChat,
					ChatComponentParser.getRawText(jsonChat), connection.getLogger());
			connection.getEventHandler().broadcast(event);
			byte position = packet.readByte();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse chat packet", e);
		}

	}

}
