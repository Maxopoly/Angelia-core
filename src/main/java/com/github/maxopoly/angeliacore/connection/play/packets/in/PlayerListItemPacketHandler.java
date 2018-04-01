package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;
import com.github.maxopoly.angeliacore.model.player.OtherPlayerManager;
import com.github.maxopoly.angeliacore.model.player.PlayerProperty;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlayerListItemPacketHandler extends AbstractIncomingPacketHandler {

	public PlayerListItemPacketHandler(ServerConnection connection) {
		super(connection, 0x2E);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		int action = packet.readVarInt();
		int playerCount = packet.readVarInt();
		OtherPlayerManager manager = connection.getOtherPlayerManager();
		try {
			for (int i = 0; i < playerCount; i++) {
				UUID uuid = packet.readUUID();
				OnlinePlayer existingPlayer = manager.getPlayer(uuid);
				switch (action) {
					case 0:
						// add player
						String name = packet.readString();
						int propertyCount = packet.readVarInt();
						List<PlayerProperty> properties = new LinkedList<PlayerProperty>();
						for (int k = 0; k < propertyCount; k++) {
							String propName = packet.readString();
							String propValue = packet.readString();
							boolean isSigned = packet.readBoolean();
							String signature = isSigned ? packet.readString() : null;
							PlayerProperty prop = new PlayerProperty(propName, propValue, isSigned, signature);
							properties.add(prop);
						}
						int gameMode = packet.readVarInt();
						int ping = packet.readVarInt();
						boolean hasDisplayName = packet.readBoolean();
						String displayName = hasDisplayName ? packet.readString() : null;
						OnlinePlayer player = new OnlinePlayer(uuid, name, properties, gameMode, ping, displayName);
						manager.addPlayer(player);
						break;
					case 1:
						// update gamemode
						int updatedGameMode = packet.readVarInt();
						if (existingPlayer != null) {
							existingPlayer.setGameMode(updatedGameMode);
						}
						break;
					case 2:
						// update latency
						int updatedPing = packet.readVarInt();
						if (existingPlayer != null) {
							existingPlayer.updatePing(updatedPing);
						}
						break;
					case 3:
						// update displayName
						boolean hasUpdatedDisplayName = packet.readBoolean();
						String updatedDisplayName = hasUpdatedDisplayName ? packet.readString() : null;
						if (existingPlayer != null) {
							existingPlayer.setDisplayName(updatedDisplayName);
						}
						break;
					case 4:
						// remove player
						manager.removePlayer(uuid);
				}
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse online player packet", e);
		}

	}
}
