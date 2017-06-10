package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.HeldItemChangePacket;
import java.io.IOException;

public class ChangeSelectedItem extends AbstractAction {

	private int slot;

	public ChangeSelectedItem(ServerConnection connection, int slot) {
		super(connection);
		this.slot = slot;
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new HeldItemChangePacket(slot));
			connection.getPlayerStatus().setSelectedHotbarSlot(slot);
		} catch (IOException e) {
			connection.getLogger().error("Failed to update item held", e);
		}
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
