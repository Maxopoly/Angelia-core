package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.UseItemPacket;
import java.io.IOException;

/**
 * Basically presses right click
 *
 */
public class Interact extends AbstractAction {

	public Interact(ServerConnection connection) {
		super(connection);
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new UseItemPacket());
		} catch (IOException e) {
			connection.getLogger().error("Failed to send interact packet", e);
		}

	}

	@Override
	public boolean isDone() {
		return true;
	}

}
