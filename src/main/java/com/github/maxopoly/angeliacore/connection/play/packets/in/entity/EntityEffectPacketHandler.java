package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.model.potion.PotionEffect;
import com.github.maxopoly.angeliacore.model.potion.PotionType;

public class EntityEffectPacketHandler extends AbstractIncomingPacketHandler {

	public EntityEffectPacketHandler(ServerConnection connection) {
		super(connection, 0x4F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityId = packet.readVarInt();
			if (entityId != connection.getPlayerStatus().getID()) {
				// we only care about the player
				return;
			}
			byte effectID = packet.readByte();
			byte amplifier = packet.readByte();
			int duration = packet.readVarInt();
			byte flag = packet.readByte();
			connection.getPlayerStatus().addPotionEffect(
					new PotionEffect(PotionType.getById(effectID), amplifier + 1, duration));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse entity effect packet", e);
		}
	}

}
