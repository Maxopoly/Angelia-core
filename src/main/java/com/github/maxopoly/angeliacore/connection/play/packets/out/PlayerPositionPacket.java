package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.binary.WriteOnlyPacket;
import com.github.maxopoly.angeliacore.model.location.Location;
import java.io.IOException;

public class PlayerPositionPacket extends WriteOnlyPacket {

	public PlayerPositionPacket(double x, double y, double z, boolean onGround) throws IOException {
		super(0x0D);
		writeDouble(x);
		writeDouble(y);
		writeDouble(z);
		writeBoolean(onGround);
	}

	public PlayerPositionPacket(Location loc, boolean onGround) throws IOException {
		this(loc.getX(), loc.getY(), loc.getZ(), onGround);
	}

}
