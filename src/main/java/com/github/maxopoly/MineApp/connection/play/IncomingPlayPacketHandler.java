package com.github.maxopoly.MineApp.connection.play;

import com.github.maxopoly.MineApp.connection.ServerConnection;
import com.github.maxopoly.MineApp.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.MineApp.connection.play.packets.in.ChatMessagePacketHandler;
import com.github.maxopoly.MineApp.connection.play.packets.in.KeepAlivePacketHandler;
import com.github.maxopoly.MineApp.connection.play.packets.in.PlayerPositionLookPacketHandler;
import com.github.maxopoly.MineApp.packet.ReadOnlyPacket;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class IncomingPlayPacketHandler implements Runnable {

	private static final long TIMEOUT = 20000;

	private Map<Integer, AbstractIncomingPacketHandler> handlerMap;
	private ServerConnection connection;
	private long lastKeepAlive;

	public IncomingPlayPacketHandler(ServerConnection connection) {
		this.connection = connection;
		this.handlerMap = new TreeMap<Integer, AbstractIncomingPacketHandler>();
		registerAllHandler();
	}

	private void registerAllHandler() {
		registerPacketHandler(new KeepAlivePacketHandler(connection));
		registerPacketHandler(new PlayerPositionLookPacketHandler(connection));
		registerPacketHandler(new ChatMessagePacketHandler(connection));
	}

	private void processPacket(ReadOnlyPacket packet) {
		int packetID = packet.getPacketID();
		AbstractIncomingPacketHandler properHandler = handlerMap.get(packetID);
		if (properHandler != null) {
			properHandler.handlePacket(packet);
		}
		// we just skip the packet if we dont have a proper handler
	}

	private void registerPacketHandler(AbstractIncomingPacketHandler handler) {
		// handler should only be registered in the method for registering all handlers, not during runtime
		handlerMap.put(handler.getIDHandled(), handler);
	}

	public boolean hasHandler(int packetID) {
		return handlerMap.get(packetID) != null;
	}

	public void updateKeepAlive() {
		lastKeepAlive = System.currentTimeMillis();
	}

	@Override
	public void run() {
		lastKeepAlive = System.currentTimeMillis();
		while (true) {
			if ((System.currentTimeMillis() - lastKeepAlive) > TIMEOUT) {
				// no ping for 20 sec, let's assume the server is gone
				// TODO handle timeout
				connection.getLogger().info("Disconnected from " + connection.getAdress() + " due to timeout");
				break;
			}
			if (connection.dataAvailable()) {
				try {
					processPacket(connection.getPacket());
				} catch (IOException e) {
					connection.getLogger().error("Failed to get packet from connection", e);
					// TODO break loop?
				}
			} else {
				try {
					// TODO think about a good/better time for this
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
