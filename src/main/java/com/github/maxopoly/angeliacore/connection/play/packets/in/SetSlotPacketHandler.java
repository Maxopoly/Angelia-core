package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.inventory.UpdateInventorySlotEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class SetSlotPacketHandler extends AbstractIncomingPacketHandler {

	public SetSlotPacketHandler(ServerConnection connection) {
		super(connection, 0x16);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readByte();
			short slot = packet.readShort();
			ItemStack is = packet.readItemStack();
			Inventory inv = connection.getPlayerStatus().getInventory(windowID);
			if (inv != null && slot >= 0 && slot < inv.getSize()) {
				connection.getEventHandler().broadcast(new UpdateInventorySlotEvent(inv, windowID, slot, is));
				inv.updateSlot(slot, is);
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse set slot packet", e);
		}

	}

}
