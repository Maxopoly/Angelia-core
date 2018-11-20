package com.github.maxopoly.angeliacore.connection;

import com.github.maxopoly.angeliacore.actions.ActionQueue;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.binary.WriteOnlyPacket;
import com.github.maxopoly.angeliacore.block.ChunkHolder;
import com.github.maxopoly.angeliacore.config.GlobalConfig;
import com.github.maxopoly.angeliacore.connection.login.AuthenticationHandler;
import com.github.maxopoly.angeliacore.connection.login.EncryptionHandler;
import com.github.maxopoly.angeliacore.connection.login.GameJoinHandler;
import com.github.maxopoly.angeliacore.connection.login.HandShake;
import com.github.maxopoly.angeliacore.connection.play.EntityManager;
import com.github.maxopoly.angeliacore.connection.play.Heartbeat;
import com.github.maxopoly.angeliacore.connection.play.ItemTransactionManager;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ClientSettingPacket;
import com.github.maxopoly.angeliacore.encryption.AES_CFB8_Encrypter;
import com.github.maxopoly.angeliacore.event.EventBroadcaster;
import com.github.maxopoly.angeliacore.exceptions.MalformedCompressedDataException;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.player.OtherPlayerManager;
import com.github.maxopoly.angeliacore.plugin.AngeliaPlugin;
import com.github.maxopoly.angeliacore.plugin.PluginManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Timer;
import java.util.UUID;

import org.apache.logging.log4j.Logger;

/**
 * Represents a connection to a server. Attributes may contain invalid/null
 * values before the connect() method was called and should only be accessed
 * afterwards (except for constructor fields)
 *
 */
public class ServerConnection {

	private String serverAdress;
	private AuthenticationHandler authHandler;
	private AES_CFB8_Encrypter syncEncryptionHandler;
	private Logger logger;
	private int port;
	private Socket socket;
	private Heartbeat playPacketHandler;
	private ThePlayer playerStatus;
	private EventBroadcaster eventHandler;
	private ActionQueue actionQueue;
	private Timer tickTimer;
	private ItemTransactionManager transActionManager;
	private PluginManager pluginManager;
	private OtherPlayerManager otherPlayerManager;
	private ChunkHolder chunkHolder;
	private EntityManager entityManager;
	private GlobalConfig config;
	private boolean localHost;

	private boolean encryptionEnabled;
	private boolean compressionEnabled;
	private int maximumUncompressedPacketSize;
	private int protocolVersion;
	private int tickDelay; // in ms

	private DataInputStream input;
	private DataOutputStream output;
	private boolean closed;

	/**
	 * Standard Constructor
	 *
	 * @param adress IP or domain of the server
	 * @param port   Port of the server
	 * @param logger Logger to use
	 * @param auth   Account authentication to use
	 */
	public ServerConnection(String adress, int port, Logger logger, AuthenticationHandler auth) {
		this.serverAdress = adress;
		this.port = port;
		this.logger = logger;
		this.closed = false;
		this.encryptionEnabled = false;
		this.compressionEnabled = false;
		this.authHandler = auth;
		this.tickDelay = 50;
		this.protocolVersion = -1;
		this.eventHandler = new EventBroadcaster(logger);
		this.transActionManager = new ItemTransactionManager();
		this.config = new GlobalConfig(this, logger, new File ("angeliaData/"));
		this.localHost = "localhost".equalsIgnoreCase(adress) || "127.0.0.1".equals(adress);
	}

	/**
	 * Constructor with the default port 25565
	 *
	 * @param adress IP or domain of the server
	 * @param logger Logger to use
	 * @param auth   Account authentication to use
	 */
	public ServerConnection(String adress, Logger logger, AuthenticationHandler auth) {
		this(adress, 25565, logger, auth); // default port
	}

	/**
	 * Dumps the current connection and starts over with a new socket
	 */
	private void reestablishConnection() throws IOException {
		this.socket = new Socket();
		InetSocketAddress host = new InetSocketAddress(serverAdress, port);
		try {
			socket.connect(host, 3000);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (ConnectException e) {
			throw new IOException("Failed to connect to " + serverAdress + ":" + port);
		} catch (IOException e) {
			logger.error("Exception occured", e);
			throw new IOException("Failed to connect to " + serverAdress + ":" + port);
		}
	}

	/**
	 * Does everything from 0 to 100 needed for a working connection to a server.
	 * Initially this checks whether the provided account auth is still valid,
	 * refreshes it if needed and returns if it can't be refreshed. Next it
	 * handshakes the server to get its protocol version, resets the connection and
	 * begin a new login handshake with the retrieved protocol version. Note that no
	 * actual adjustments are made based on the protocol version sent by the server,
	 * it's just copied and assumed to be our version. After the initial version
	 * handshake, we exchange encryption details, authenticate the connection
	 * attempt against Yggdrassil's (minecraft auth) server, enable sync encryption,
	 * enable compression if requested by the server, join the game and finally
	 * setup packet handlers for all kinds of incoming packets. The packet handling
	 * happens in a freshly spawned thread, which also handles consuming actions
	 * from the ActionQueue, this method will return once the connection is fully
	 * set up
	 *
	 * @throws IOException If something goes wrong
	 */
	public void connect() throws IOException {
		if (!authHandler.validateToken(logger)) {
			logger.info("Token for " + authHandler.getPlayerName() + " is no longer valid, refreshing it");
			authHandler.refreshToken(logger);
		}
		logger.info("Initializing connection process for account " + authHandler.getPlayerName() + " to " + serverAdress
				+ ":" + port);
		// connect
		reestablishConnection();
		HandShake shake = new HandShake(this);
		if (protocolVersion == -1) {
			logger.info("Connected socket to " + serverAdress);
			// handshake
			logger.info("Requesting protocol version from " + serverAdress);
			protocolVersion = shake.requestProtocolVersion();
			// we need to set up a new socket after protocol test handshaking as we want to
			// properly connect now, which
			// the server wouldnt allow right away on the same connection
			reestablishConnection();
		}
		logger.info("Sending handshake to " + serverAdress);
		shake.send(true, protocolVersion);
		// begin login
		logger.info("Beginning login to " + serverAdress);
		shake.sendLoginStartMessage(authHandler.getPlayerName());
		// figure out encryption secret
		logger.info("Parsing encryption request from " + serverAdress);
		EncryptionHandler asyncEncHandler = new EncryptionHandler(this);
		if (!asyncEncHandler.parseEncryptionRequest()) {
			logger.info("Failed to handle encryption request, could not setup connection");
			close(DisconnectReason.Unknown_Connection_Error);
			return;
		}
		asyncEncHandler.genSecretKey();
		logger.info("Authenticating connection attempt to " + serverAdress + " against Yggdrassil session server");
		authHandler.authAgainstSessionServer(asyncEncHandler.generateKeyHash(), logger);
		logger.info("Sending encryption reply to " + serverAdress);
		asyncEncHandler.sendEncryptionResponse();
		// everything from here on is encrypted
		logger.info("Enabling sync encryption with " + serverAdress);
		encryptionEnabled = true;
		syncEncryptionHandler = new AES_CFB8_Encrypter(asyncEncHandler.getSharedSecret(),
				asyncEncHandler.getSharedSecret());
		GameJoinHandler joinHandler = new GameJoinHandler(this);
		joinHandler.parseLoginSuccess();
		// if we reach this point, we successfully logged in and the connection state
		// switches to PLAY, so from now on
		// everything is handled by our standard packet handler
		logger.info("Switching connection to play state");
		playerStatus = new ThePlayer(this);
		playPacketHandler = new Heartbeat(this);
		pluginManager = new PluginManager(this);
		actionQueue = new ActionQueue(this);
		otherPlayerManager = new OtherPlayerManager();
		chunkHolder = new ChunkHolder(config);
		entityManager = new EntityManager();
		tickTimer = new Timer("Angelia tick");
		tickTimer.schedule(playPacketHandler, tickDelay, tickDelay);
		// still have to do this
		sendPacket(new ClientSettingPacket());
	}

	/**
	 * Sends a packet to the server. This method also handles both compression and
	 * encryption
	 *
	 * @param packet Packet to send
	 * @throws IOException
	 */
	public void sendPacket(WriteOnlyPacket packet) throws IOException {
		synchronized (output) {
			byte[] data;
			int packetSize = packet.getSize();
			if (compressionEnabled) {
				if (packetSize > maximumUncompressedPacketSize) {
					WriteOnlyPacket compressedPacket = new WriteOnlyPacket();
					byte[] compressedData = CompressionManager.compressZLib(packet.toByteArray());
					// write length of uncompressed data
					compressedPacket.writeVarInt(packet.getSize());
					// write compressed data
					compressedPacket.writeBytes(compressedData);
					// standard append length of total packet at the front to finish compression
					data = compressedPacket.toByteArrayIncludingLength();
				} else {
					// no compression in which case we write 0 as uncompressed size and directly
					// copy the data
					WriteOnlyPacket fakeCompressedPacket = new WriteOnlyPacket();
					fakeCompressedPacket.writeByte((byte) 0);
					fakeCompressedPacket.writeBytes(packet.toByteArray());
					data = fakeCompressedPacket.toByteArrayIncludingLength();
				}
			} else {
				data = packet.toByteArrayIncludingLength();
			}
			if (encryptionEnabled) {
				data = syncEncryptionHandler.encrypt(data);
			}
			try {
				output.write(data);
			} catch (SocketException e) {
				close(DisconnectReason.Unknown_Connection_Error);
			}
		}
	}

	public boolean isLocalHost() {
		return localHost;
	}

	/**
	 * Gets a received packet if one is available and waits (non-blocking) until one
	 * is available if none is available.
	 *
	 * @return Packet read
	 * @throws MalformedCompressedDataException
	 */
	public ReadOnlyPacket getPacket() throws IOException, MalformedCompressedDataException {
		synchronized (input) {
			int packetLength = 0;
			int j = 0;
			while (true) {
				byte b = input.readByte();
				if (encryptionEnabled) {
					b = syncEncryptionHandler.decrypt(new byte[] { b })[0];
				}
				packetLength |= (b & 0x7F) << j++ * 7;
				if (j > 5) {
					throw new RuntimeException("VarInt too big");
				}
				if ((b & 0x80) != 128) {
					break;
				}
			}
			// packetLength is parsed here
			byte[] dataArray = new byte[packetLength];
			input.readFully(dataArray);
			if (encryptionEnabled) {
				dataArray = syncEncryptionHandler.decrypt(dataArray);
			}
			if (!compressionEnabled) {
				return new ReadOnlyPacket(dataArray);
			}
			// compression is enabled if we reach this
			// parse varint specifying the length of the uncompressed data
			int index = 0;
			int uncompressedPacketLength = 0;
			j = 0;
			while (true) {
				byte b = dataArray[index++];
				uncompressedPacketLength |= (b & 0x7F) << j++ * 7;
				if (j > 5) {
					throw new RuntimeException("VarInt too big");
				}
				if ((b & 0x80) != 128) {
					break;
				}
			}
			// cut out varint from actual data
			dataArray = Arrays.copyOfRange(dataArray, index, dataArray.length);
			if (uncompressedPacketLength == 0) {
				// if uncompressed length is set to zero, packet was uncompressed
				return new ReadOnlyPacket(dataArray);
			}
			byte[] decompressedData;
			decompressedData = CompressionManager.decompressZLib(dataArray, logger);
			if (decompressedData.length != uncompressedPacketLength) {
				throw new MalformedCompressedDataException(
						"Decompression failed and result in incorrect packet length");
			}
			return new ReadOnlyPacket(decompressedData);
		}
	}

	public boolean dataAvailable() throws IOException {
		synchronized (input) {
			return input.available() != 0;
		}
	}

	/**
	 * Closes this connection irrevertably
	 */
	public void close(DisconnectReason reason) {
		try {
			logger.info("Closing socket with " + serverAdress);
			if (!socket.isClosed()) {
				socket.close();
			}
			if (tickTimer != null) {
				tickTimer.cancel();
				tickTimer.purge();
			}
			if (pluginManager != null) {
				pluginManager.shutDown();
			}
			closed = true;
		} catch (IOException e) {
			// its ok, probably
		}
		ActiveConnectionManager.getInstance().reportDisconnect(this, reason);
	}

	public void activateCompression(int maximumUncompressedSize) {
		logger.info("Enabling compression with " + maximumUncompressedSize
				+ " as compression threshhold for connection to " + serverAdress);
		this.compressionEnabled = true;
		this.maximumUncompressedPacketSize = maximumUncompressedSize;
	}

	/**
	 * @return Adress (Domain or IP) this connection is connected to
	 */
	public String getAdress() {
		return serverAdress;
	}

	/**
	 * @return Object containing all known information regarding the state of the
	 *         player
	 */
	public ThePlayer getPlayerStatus() {
		return playerStatus;
	}

	/**
	 * @return Handler for incoming packets while connection is in play state
	 */
	public Heartbeat getIncomingPlayPacketHandler() {
		return playPacketHandler;
	}

	/**
	 * @return Logging instance used by this connection
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return Manager for item transactions
	 */
	public ItemTransactionManager getItemTransActionManager() {
		return transActionManager;
	}

	/**
	 * @return Port used, 25565 is default
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return Whether this connection was closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @return This connection's plugin manager
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * @return Connections action queue
	 */
	public ActionQueue getActionQueue() {
		return actionQueue;
	}

	/**
	 * @return Manager which holds chunk data and is gate way for all block data
	 *         access
	 */
	public ChunkHolder getChunkHolder() {
		return chunkHolder;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 *
	 * @return EventHandler for registering listeners and calling events
	 */
	public EventBroadcaster getEventHandler() {
		return eventHandler;
	}

	public OtherPlayerManager getOtherPlayerManager() {
		return otherPlayerManager;
	}

	/**
	 * @return How often the connection is ticked per second
	 */
	public double getTicksPerSecond() {
		return 1000 / (double) tickDelay;
	}

	/**
	 * @return Mojang side authentication of the player
	 */
	AuthenticationHandler getAuthHandler() {
		return authHandler;
	}
	
	/**
	 * @return Clientside only configuration manager
	 */
	public GlobalConfig getConfig() {
		return config;
	}

	/**
	 * @return The name of the player connected
	 */
	public String getPlayerName() {
		return authHandler.getPlayerName();
	}

	/**
	 * @return The UUID of the player connected
	 */
	public UUID getPlayerUUID() {
		String withoutDash = authHandler.getPlayerUUID();
		return UUID.fromString(withoutDash.substring(0, 8) + "-" + withoutDash.substring(8, 12) + "-"
				+ withoutDash.substring(12, 16) + "-" + withoutDash.substring(16, 24) + "-" + withoutDash.substring(24, 32));
	}
}
