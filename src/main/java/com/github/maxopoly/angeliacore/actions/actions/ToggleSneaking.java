package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.EntityActionPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.EntityActionPacket.Action;

public class ToggleSneaking extends AbstractAction {

	private boolean sneaking;

	public ToggleSneaking(ServerConnection connection, boolean sneaking) {
		super(connection);
		this.sneaking = sneaking;
	}

	@Override
	public void execute() {
		int entityId = connection.getPlayerStatus().getID();
		EntityActionPacket.Action action = sneaking ? Action.START_SNEAKING : Action.STOP_SNEAKING;
		try {
			connection.sendPacket(new EntityActionPacket(entityId, action));
		} catch (IOException error) {
			connection.getLogger().error("Failed to send sneaking toggle packet.", error);
			return;
		}
		connection.getPlayerStatus().setSneaking(sneaking);
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[0];
	}

	@Override
	public boolean isDone() {
		return true;
	}

}