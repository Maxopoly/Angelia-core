package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.CloseWindowPacket;
import java.io.IOException;

public class CloseInventoryWindow extends AbstractAction {

	private byte windowID;

	public CloseInventoryWindow(ServerConnection connection, byte windowID) {
		super(connection);
		this.windowID = windowID;
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new CloseWindowPacket(windowID));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send inventory close packet", e);
		}
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
