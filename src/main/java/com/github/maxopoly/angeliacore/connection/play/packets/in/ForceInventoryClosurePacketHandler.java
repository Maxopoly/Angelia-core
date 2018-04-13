package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.InventoryClosureEvent;

public class ForceInventoryClosurePacketHandler extends AbstractIncomingPacketHandler {

	public ForceInventoryClosurePacketHandler(ServerConnection connection) {
		super(connection, 0x12);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte invId = packet.readByte();
			connection.getEventHandler().broadcast(
					new InventoryClosureEvent(invId, connection.getPlayerStatus().getInventory(invId)));
			connection.getPlayerStatus().removeOpenInventory(invId);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse inv closure packet", e);
		}

	}
}
