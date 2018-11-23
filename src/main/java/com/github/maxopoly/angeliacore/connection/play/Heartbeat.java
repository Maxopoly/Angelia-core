package com.github.maxopoly.angeliacore.connection.play;

import com.github.maxopoly.angeliacore.connection.DisconnectReason;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.*;
import com.github.maxopoly.angeliacore.connection.play.packets.in.entity.EntityEffectPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.entity.EntityLookAndRelativeMovePacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.entity.EntityLookPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.entity.EntityPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.entity.EntityRelativeMovePacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.entity.EntityTeleportPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
import com.github.maxopoly.angeliacore.exceptions.MalformedCompressedDataException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

public class Heartbeat extends TimerTask {

	private static final long TIMEOUT = 60000;
	private static final long POSITION_UPDATE_INTERVALL = 1000;

	private Map<Integer, AbstractIncomingPacketHandler> handlerMap;
	private ServerConnection connection;
	private long lastKeepAlive;
	private long lastPositionPacket;
	private long tickCounter;

	public Heartbeat(ServerConnection connection) {
		this.connection = connection;
		this.handlerMap = new TreeMap<>();
		registerAllHandler();
		this.tickCounter = 0;
	}

	/**
	 * Used to setup all native handlers for incoming packets
	 */
	private void registerAllHandler() {
		registerPacketHandler(new BlockBreakAnimationPacketHandler(connection));
		registerPacketHandler(new ChatMessagePacketHandler(connection));
		registerPacketHandler(new DestroyEntitiesPacketHandler(connection));
		registerPacketHandler(new DisconnectPacketHandler(connection));
		registerPacketHandler(new EntityEffectPacketHandler(connection));
		registerPacketHandler(new EntityLookAndRelativeMovePacketHandler(connection));
		registerPacketHandler(new EntityLookPacketHandler(connection));
		registerPacketHandler(new EntityPacketHandler(connection));
		registerPacketHandler(new EntityRelativeMovePacketHandler(connection));
		registerPacketHandler(new EntityTeleportPacketHandler(connection));
		registerPacketHandler(new ForceInventoryClosurePacketHandler(connection));
		registerPacketHandler(new HealthChangeHandler(connection));
		registerPacketHandler(new JoinGamePacketHandler(connection));
		registerPacketHandler(new KeepAlivePacketHandler(connection));
		registerPacketHandler(new OpenInventoryPacketHandler(connection));
		registerPacketHandler(new PlayerListItemPacketHandler(connection));
		registerPacketHandler(new PlayerPositionLookPacketHandler(connection));
		registerPacketHandler(new SetSlotPacketHandler(connection));
		registerPacketHandler(new SpawnPlayerPacketHandler(connection));
		registerPacketHandler(new TransActionConfirmationPacketHandler(connection));
		registerPacketHandler(new WindowItemsPacketHandler(connection));
		registerPacketHandler(new XPChangeHandler(connection));
		registerPacketHandler(new ChunkDataPacketHandler(connection));
	}

	/**
	 * Handles an incoming packet by forwarding it to the right handler based on it's id, if one exists
	 *
	 * @param packet
	 *          Packet to handle
	 */
	private void processPacket(ReadOnlyPacket packet) {
		int packetID = packet.getPacketID();
		AbstractIncomingPacketHandler properHandler = handlerMap.get(packetID);
		if (properHandler != null) {
			try {
				properHandler.handlePacket(packet);
			}
			catch (Exception e) {
				//not nice to catch Exception, but we dont want to crash the entire application 
				//if an exception occurs here and there are just way too many that possibly could occur
				connection.getLogger().warn("Exception occured handling packet", e);
			}
		}
		// we just skip the packet if we dont have a proper handler
	}

	/**
	 * Registers a new packet handler for a specific packet id
	 *
	 * @param handler
	 *          Handler to register
	 */
	private void registerPacketHandler(AbstractIncomingPacketHandler handler) {
		// handler should only be registered in the method for registering all handlers, not during runtime
		handlerMap.put(handler.getIDHandled(), handler);
	}

	/**
	 * Checks whether an explicit handler exists for the given packet id
	 *
	 * @param packetID
	 *          ID to check for
	 * @return True if a handler was registered for the given ID, false otherwise
	 */
	public boolean hasHandler(int packetID) {
		return handlerMap.get(packetID) != null;
	}

	/**
	 * Updates the timestamp of the last contact with the server to the current time
	 */
	public void updateKeepAlive() {
		lastKeepAlive = System.currentTimeMillis();
	}

	/**
	 * Handles both incoming packets, keep alive of the connection and progressing pending actions in the ActionQueue
	 */
	@Override
	public void run() {
		if (!connection.isClosed()) {
			if (lastKeepAlive > 0 && (System.currentTimeMillis() - lastKeepAlive) > TIMEOUT) {
				// no ping for 10 sec, let's assume the server is gone
				connection.getLogger().info("Disconnected from " + connection.getAdress() + " due to timeout");
				connection.close(DisconnectReason.Server_Timed_Out);
				return;
			}
			long now = System.currentTimeMillis();
			if (now - lastPositionPacket > POSITION_UPDATE_INTERVALL) {
				//send every second where player is and if he is midair
				try {
					connection.sendPacket(new PlayerPositionPacket(connection.getPlayerStatus().getLocation(), connection
							.getPlayerStatus().isOnGround()));
					lastPositionPacket = now;
				} catch (IOException e1) {
					connection.getLogger().error("Failed to send player state/position packet", e1);
					connection.getLogger().error("Failed to send player state/position, connection seems to be gone");
					connection.close(DisconnectReason.Unknown_Connection_Error);
					return;
				}
			}
			// parse available data
			try {
				while (connection.dataAvailable()) {
					processPacket(connection.getPacket());
				}
			} catch (IOException e) {
				connection.getLogger().error("Failed to get packet from connection, connection seems to be gone", e);
				connection.close(DisconnectReason.Unknown_Connection_Error);
				return;
			} catch (MalformedCompressedDataException e) {
				connection.getLogger().error("Received packet with faulty compession, ignoring it", e);
			}
			// tick the action queue
			connection.getActionQueue().tick();
			++tickCounter;
		}
	}
	
	/**
	 * Counter that starts at 0 when the connection is established and is incremented every tick
	 * @return Tick counter
	 */
	public long getTickCounter() {
		return tickCounter;
	}

}
