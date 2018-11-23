package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.EventBroadcaster;
import com.github.maxopoly.angeliacore.event.events.tablist.OtherPlayerDisplayNameChangeEvent;
import com.github.maxopoly.angeliacore.event.events.tablist.OtherPlayerGameModeUpdateEvent;
import com.github.maxopoly.angeliacore.event.events.tablist.OtherPlayerJoinEvent;
import com.github.maxopoly.angeliacore.event.events.tablist.OtherPlayerLeaveGameEvent;
import com.github.maxopoly.angeliacore.event.events.tablist.OtherPlayerPingUpdateEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.player.GameMode;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;
import com.github.maxopoly.angeliacore.model.player.OtherPlayerManager;
import com.github.maxopoly.angeliacore.model.player.PlayerProperty;

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
		EventBroadcaster eventHandler = connection.getEventHandler();
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
					int gameModeInt = packet.readVarInt();
					GameMode gameMode = GameMode.parse(gameModeInt);
					int ping = packet.readVarInt();
					boolean hasDisplayName = packet.readBoolean();
					String displayName = hasDisplayName ? packet.readString() : null;
					OnlinePlayer player = new OnlinePlayer(uuid, name, properties, gameMode, ping, displayName);
					eventHandler.broadcast(new OtherPlayerJoinEvent(player));
					manager.addPlayer(player);
					break;
				case 1:
					// update gamemode
					int updatedGameModeInt = packet.readVarInt();
					GameMode updatedgameMode = GameMode.parse(updatedGameModeInt);
					if (existingPlayer != null) {
						eventHandler.broadcast(new OtherPlayerGameModeUpdateEvent(existingPlayer, updatedgameMode));
						existingPlayer.setGameMode(updatedgameMode);
					}
					break;
				case 2:
					// update latency
					int updatedPing = packet.readVarInt();
					if (existingPlayer != null) {
						eventHandler.broadcast(new OtherPlayerPingUpdateEvent(existingPlayer, updatedPing));
						existingPlayer.updatePing(updatedPing);
					}
					break;
				case 3:
					// update displayName
					boolean hasUpdatedDisplayName = packet.readBoolean();
					String updatedDisplayName = hasUpdatedDisplayName ? packet.readString() : null;
					if (existingPlayer != null) {
						eventHandler
								.broadcast(new OtherPlayerDisplayNameChangeEvent(existingPlayer, updatedDisplayName));
						existingPlayer.setDisplayName(updatedDisplayName);
					}
					break;
				case 4:
					// remove player
					if (existingPlayer != null) {
						eventHandler.broadcast(new OtherPlayerLeaveGameEvent(existingPlayer));
						manager.removePlayer(uuid);
					}
				}
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse player list item packet", e);
		}

	}
}
