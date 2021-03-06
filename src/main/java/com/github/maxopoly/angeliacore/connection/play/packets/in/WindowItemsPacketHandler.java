package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.inventory.InventoryInitializationEvent;
import com.github.maxopoly.angeliacore.event.events.inventory.UpdateInventoryEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class WindowItemsPacketHandler extends AbstractIncomingPacketHandler {

	public WindowItemsPacketHandler(ServerConnection connection) {
		super(connection, 0x14);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readByte();
			short count = packet.readShort();
			ItemStack[] items = new ItemStack[count];
			Inventory inv = connection.getPlayerStatus().getInventory(windowID);
			for (int i = 0; i < count; i++) {
				items[i] = packet.readItemStack();
			}
			if (inv != null) {
				boolean wasInitialized = inv.isInitialized();
				connection.getEventHandler().broadcast(new UpdateInventoryEvent(inv, windowID, items));
				inv.setSlots(items);
				if (!wasInitialized) {
					connection.getEventHandler().broadcast(new InventoryInitializationEvent(inv, windowID));
				}
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse window items packet", e);
		}

	}

}
