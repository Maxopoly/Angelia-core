package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.EntityActionPacket;
import com.github.maxopoly.angeliacore.model.location.Location;

public class SprintTo extends AbstractAction { 
	
	public static final int START_SPRINT_ID = 3;
	public static final int STOP_SPRINT_ID = 4;
	
	private SequentialActionExecution actions;
	private Location destination;

	public SprintTo(ServerConnection connection, Location desto) {
		super(connection);
		this.destination = desto;
	}

	@Override
	public void execute() {
		if (actions == null) {
			setupActions();
			actions.execute();
		}
		actions.execute();
	}

	@Override
	public boolean isDone() {
		if (actions == null) {
			setupActions();
		}
		return actions.isDone();
	}

	private void setupActions() {
		this.actions = new SequentialActionExecution(new LookAt(connection, destination.getBlockCenter()),
				new MoveTo(connection, destination, MoveTo.SPRINTING_SPEED));
		try {
			connection.sendPacket(new EntityActionPacket(connection.getPlayerStatus().getEntityID(), START_SPRINT_ID, 0));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send stop sprint packet ", e);
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.LOOKING_DIRECTION, ActionLock.MOVEMENT };
	}

	@Override
	public void finish() {
		try {
			connection.sendPacket(new EntityActionPacket(connection.getPlayerStatus().getEntityID(), STOP_SPRINT_ID, 0));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send stop sprint packet ", e);
		}
	}
	
}
