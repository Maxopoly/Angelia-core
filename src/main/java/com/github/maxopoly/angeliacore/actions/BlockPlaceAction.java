package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BlockPlacementPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.model.Location;
import java.io.IOException;

public class BlockPlaceAction extends AbstractAction {

	private Location location;
	private int face;

	public BlockPlaceAction(ServerConnection connection, Location loc, int face) {
		super(connection);
		this.location = loc;
		this.face = face;
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new BreakAnimationPacket());
			connection.sendPacket(new BlockPlacementPacket(location, face));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send blockplace packet", e);
		}

	}

	@Override
	public boolean isDone() {
		return true; // always 1 tick
	}

}
