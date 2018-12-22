package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BlockPlacementPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;

public class PlaceBlock extends AbstractAction {

	private Location location;
	private BlockFace face;

	public PlaceBlock(ServerConnection connection, Location loc, BlockFace face) {
		super(connection);
		this.location = loc;
		this.face = face;
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new BreakAnimationPacket());
			connection.sendPacket(new BlockPlacementPacket(location.toBlockLocation(), face));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send blockplace packet", e);
		}

	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

	@Override
	public boolean isDone() {
		return true; // always 1 tick
	}

}
