package com.github.maxopoly.angeliacore.connection.play;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.ChatMessagePacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.DisconnectPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.HealthChangeHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.KeepAlivePacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.PlayerPositionLookPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.SetSlotPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.TransActionConfirmationPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.WindowItemsPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.in.XPChangeHandler;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerStatePacket;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;
import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;

public class Heartbeat extends TimerTask {

	private static final long TIMEOUT = 10000;

	private Map<Integer, AbstractIncomingPacketHandler> handlerMap;
	private ServerConnection connection;
	private long lastKeepAlive;

	public Heartbeat(ServerConnection connection) {
		this.connection = connection;
		this.handlerMap = new TreeMap<Integer, AbstractIncomingPacketHandler>();
		registerAllHandler();
	}

	/**
	 * Used to setup all native handlers for incoming packets
	 */
	private void registerAllHandler() {
		registerPacketHandler(new KeepAlivePacketHandler(connection));
		registerPacketHandler(new PlayerPositionLookPacketHandler(connection));
		registerPacketHandler(new ChatMessagePacketHandler(connection));
		registerPacketHandler(new XPChangeHandler(connection));
		registerPacketHandler(new HealthChangeHandler(connection));
		registerPacketHandler(new DisconnectPacketHandler(connection));
		registerPacketHandler(new WindowItemsPacketHandler(connection));
		registerPacketHandler(new SetSlotPacketHandler(connection));
		registerPacketHandler(new TransActionConfirmationPacketHandler(connection));
		// no use for block break animation right now, as it only tells us about other peoples breaking
		// registerPacketHandler(new BlockBreakAnimationPacketHandler(connection));
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
			properHandler.handlePacket(packet);
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
		lastKeepAlive = System.currentTimeMillis();
		if (!connection.isClosed()) {
			if ((System.currentTimeMillis() - lastKeepAlive) > TIMEOUT) {
				// no ping for 10 sec, let's assume the server is gone
				connection.getLogger().info("Disconnected from " + connection.getAdress() + " due to timeout");
				connection.close();
			}
			try {
				connection.sendPacket(new PlayerStatePacket());
			} catch (IOException e1) {
				connection.getLogger().error("Failed to send player state packet", e1);
				// TODO break here?
			}
			// parse available data
			while (connection.dataAvailable()) {
				try {
					processPacket(connection.getPacket());
				} catch (IOException e) {
					connection.getLogger().error("Failed to get packet from connection", e);
					// TODO break loop?
				}
			}
			// tick the action queue
			connection.getActionQueue().tick();
		}
	}

}
