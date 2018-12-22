package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.UseItemPacket;
import com.github.maxopoly.angeliacore.model.item.Hand;

/**
 * Basically presses right click
 *
 */
public class Interact extends AbstractAction {

	private Hand hand;

	public Interact(ServerConnection connection, Hand hand) {
		super(connection);
		this.hand = hand;
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new UseItemPacket(hand));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send interact packet", e);
		}

	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
