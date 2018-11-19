package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class WindowPropertyPacketHandler extends AbstractIncomingPacketHandler {
	
	public WindowPropertyPacketHandler(ServerConnection connection) {
		super(connection, 0x15);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readByte();
			Inventory inv = connection.getPlayerStatus().getInventory(windowID);
			if (inv == null) {
				return;
			}
			
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse window property packet", e);
		}
		
		
	}

}
