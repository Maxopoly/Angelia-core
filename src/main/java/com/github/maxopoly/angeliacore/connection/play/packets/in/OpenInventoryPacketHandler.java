package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.inventory.OpenInventoryEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.chat.ChatComponentParser;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class OpenInventoryPacketHandler extends AbstractIncomingPacketHandler {

	public OpenInventoryPacketHandler(ServerConnection connection) {
		super(connection, 0x13);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readByte();
			String windowType = packet.readString();
			String name = ChatComponentParser.getRawText(packet.readString());
			byte numberOfSlots = packet.readByte();
			Inventory inv = Inventory.constructInventory(windowType, name, numberOfSlots, windowID);
			if (inv != null) {
				connection.getEventHandler().broadcast(new OpenInventoryEvent(inv, windowID));
				connection.getPlayerStatus().addInventory(inv, windowID);
			} else {
				connection.getLogger()
						.warn(String.format(
								"Unsupported inventory of size %d with window type %s and name %s was opened",
								numberOfSlots, windowType, name));
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse open inventory packet", e);
		}

	}
}
