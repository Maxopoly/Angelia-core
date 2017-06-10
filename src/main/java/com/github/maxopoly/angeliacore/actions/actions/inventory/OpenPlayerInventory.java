package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ClientStatusPacket;
import java.io.IOException;

public class OpenPlayerInventory extends AbstractAction {

	public OpenPlayerInventory(ServerConnection connection) {
		super(connection);
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new ClientStatusPacket(2));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send inventory opening packet");
		}
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
