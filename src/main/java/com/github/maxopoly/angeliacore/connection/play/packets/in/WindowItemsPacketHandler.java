package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.UpdateInventoryEvent;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class WindowItemsPacketHandler extends AbstractIncomingPacketHandler {

	public WindowItemsPacketHandler(ServerConnection connection) {
		super(connection, 0x14);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readUnsignedByte();
			short count = packet.readSignedShort();
			ItemStack[] items = new ItemStack[count];
			Inventory inv = connection.getPlayerStatus().getInventory(windowID);
			for (int i = 0; i < count; i++) {
				items[i] = packet.readItemStack();
			}
			if (inv != null) {
				connection.getEventHandler().broadcast(new UpdateInventoryEvent(inv, windowID, items));
				inv.setSlots(items);
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse window items packet", e);
		}

	}

}
