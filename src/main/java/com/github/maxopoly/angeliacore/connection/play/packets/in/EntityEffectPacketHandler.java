package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.potion.PotionEffect;
import com.github.maxopoly.angeliacore.model.potion.PotionType;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class EntityEffectPacketHandler extends AbstractIncomingPacketHandler {

	public EntityEffectPacketHandler(ServerConnection connection) {
		super(connection, 0x4B);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityId = packet.readVarInt();
			if (entityId != connection.getPlayerStatus().getEntityID()) {
				// we only care about the player
				return;
			}
			byte effectID = packet.readUnsignedByte();
			byte amplifier = packet.readUnsignedByte();
			int duration = packet.readVarInt();
			byte flag = packet.readUnsignedByte();
			connection.getPlayerStatus().addPotionEffect(
					new PotionEffect(PotionType.getById(effectID), amplifier + 1, duration));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse entity effect packet", e);
		}
	}

}
