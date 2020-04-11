package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.player.HealthChangeEvent;
import com.github.maxopoly.angeliacore.event.events.player.HungerChangeEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class HealthChangeHandler extends AbstractIncomingPacketHandler {

	public HealthChangeHandler(ServerConnection connection) {
		super(connection, 0x41);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			float health = packet.readFloat();
			int food = packet.readVarInt();
			float foodSaturation = packet.readFloat();
			float oldHealth = connection.getPlayerStatus().getHealth();
			int oldFood = connection.getPlayerStatus().getHunger();
			if (health != oldHealth) {
				connection.getEventHandler().broadcast(new HealthChangeEvent(health, oldHealth));
				connection.getPlayerStatus().setHealth(health);
			}
			if (food != oldFood) {
				connection.getEventHandler().broadcast(new HungerChangeEvent(food, oldFood));
			}
			connection.getPlayerStatus().updateHunger(food, foodSaturation);

		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse health update packet", e);
		}
	}
}
