package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BlockPlacementPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;

/**
 * Sends a {@link PlaceBlock} to the server and places a block.
 *
 */
public class PlaceBlock extends AbstractAction {

	private Location location;
	private BlockFace face;

	public PlaceBlock(ServerConnection connection, Location loc, BlockFace face) {
		super(connection);
		this.location = loc;
		this.face = face;
	}

	/**
	 * Gets the block that was placed. If the action isn't done then it will return
	 * null
	 * 
	 * @return The block placed
	 */
	public BlockState getBlockPlaced() {
		if (!isDone())
			return null;
		else
			return this.location.getBlockAt(this.connection);
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
