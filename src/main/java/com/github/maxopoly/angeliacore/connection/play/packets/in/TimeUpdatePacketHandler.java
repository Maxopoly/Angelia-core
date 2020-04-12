package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.world.DayTimeUpdateEvent;
import com.github.maxopoly.angeliacore.event.events.world.WorldTimeUpdateEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.world.WorldStatusHandler;

public class TimeUpdatePacketHandler extends AbstractIncomingPacketHandler {

	public TimeUpdatePacketHandler(ServerConnection connection) {
		super(connection, 0x47);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			long worldAge = packet.readLong();
			long timeOfDay = packet.readLong();
			WorldStatusHandler statusHandler = connection.getWorldStatusHandler();
			long oldWorldAge = statusHandler.getWorldAge();
			long oldTimeOfDay = statusHandler.getTimeOfDay();
			if (oldWorldAge != worldAge) {
				connection.getEventHandler().broadcast(new WorldTimeUpdateEvent(oldWorldAge, worldAge));
			}
			if (timeOfDay != oldTimeOfDay) {
				connection.getEventHandler().broadcast(new DayTimeUpdateEvent(oldTimeOfDay, timeOfDay));
			}
			statusHandler.updateWorldTime(worldAge, timeOfDay);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse time update packet", e);
		}

	}
}
