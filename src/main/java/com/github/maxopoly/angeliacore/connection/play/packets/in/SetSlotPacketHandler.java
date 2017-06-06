package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class SetSlotPacketHandler extends AbstractIncomingPacketHandler {

	public SetSlotPacketHandler(ServerConnection connection) {
		super(connection, 0x16);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readUnsignedByte();
			short slot = packet.readSignedShort();
			ItemStack is = packet.readItemStack();
			if (windowID == 0) {
				System.out.println("Setting " + is.toString() + " for " + slot);
				connection.getPlayerStatus().getPlayerInventory().updateSlot(slot, is);
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse set slot packet", e);
		}

	}

}
