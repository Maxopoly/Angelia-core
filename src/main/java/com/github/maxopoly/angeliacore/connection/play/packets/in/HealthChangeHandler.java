package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.player.HealthChangeEvent;
import com.github.maxopoly.angeliacore.event.events.player.HungerChangeEvent;
import com.github.maxopoly.angeliacore.event.events.player.PlayerDeathEvent;
import com.github.maxopoly.angeliacore.event.events.player.SaturationChangeEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.ThePlayer;

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
			ThePlayer player = connection.getPlayerStatus();
			float oldHealth = player.getHealth();
			int oldFood = player.getHunger();
			float oldSaturation = player.getSaturation();
			player.updateHunger(food, foodSaturation);
			player.setHealth(health);
			if (health != oldHealth) {
				connection.getEventHandler().broadcast(new HealthChangeEvent(health, oldHealth));
				if (health <= 0) {
					if (!connection.getPlayerStatus().isDead()) {
						connection.getEventHandler().broadcast(new PlayerDeathEvent(player));
						player.setDead(true);
					}
				}
			}
			if (food != oldFood) {
				connection.getEventHandler().broadcast(new HungerChangeEvent( oldFood, food));
			}
			if (foodSaturation != oldSaturation) {
				connection.getEventHandler().broadcast(new SaturationChangeEvent(oldSaturation, foodSaturation));
			}

		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse health update packet", e);
		}
	}
}
