package com.github.maxopoly.angeliacore.connection;

import com.github.maxopoly.angeliacore.connection.login.AuthenticationHandler;
import com.github.maxopoly.angeliacore.connection.login.EncryptionHandler;
import com.github.maxopoly.angeliacore.connection.login.GameJoinHandler;
import com.github.maxopoly.angeliacore.connection.login.HandShake;
import com.github.maxopoly.angeliacore.connection.play.IncomingPlayPacketHandler;
import com.github.maxopoly.angeliacore.connection.play.PlayerStatus;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ClientSettingPacket;
import com.github.maxopoly.angeliacore.encryption.AES_CFB8_Encrypter;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import org.apache.logging.log4j.Logger;

public class ServerConnection {

	private String serverAdress;
	private AuthenticationHandler authHandler;
	private AES_CFB8_Encrypter syncEncryptionHandler;
	private Logger logger;
	private int port;
	private Socket socket;
	private IncomingPlayPacketHandler playPacketHandler;
	private PlayerStatus playerStatus;

	private String playerName;
	private String email;
	private String userPassword;
	private boolean encryptionEnabled;
	private boolean compressionEnabled;
	private int maximumUncompressedPacketSize;

	private DataInputStream input;
	private DataOutputStream output;

	public ServerConnection(String adress, int port, String email, String password, Logger logger) {
		this.serverAdress = adress;
		this.port = port;
		this.email = email;
		this.userPassword = password;
		this.logger = logger;
		this.encryptionEnabled = false;
		this.compressionEnabled = false;
	}

	public ServerConnection(String adress, String email, String password, Logger logger) {
		this(adress, 25565, email, password, logger); // default port
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
		} catch (IOException e) {
			logger.error("Exception occured", e);
			throw new IOException("Failed to connect to " + serverAdress + ":" + port);
		}
	}

	/**
	 * Takes all the steps from authenticating, over connecting up to setting up future packet handling
	 * 
	 * @throws IOException
	 *           If something goes wrong
	 */
	public void connect() throws IOException {
		logger.info("Initializing connection process for account " + email + " to " + serverAdress + ":" + port);
		// auth first
		authHandler = new AuthenticationHandler();
		authHandler.authenticate(email, userPassword, logger);
		this.playerName = authHandler.getPlayerName();
		logger.info("Successfully authenticated as " + playerName + " with UUID " + authHandler.getPlayerID());
		// connect
		reestablishConnection();
		logger.info("Connected socket to " + serverAdress);
		// handshake
		HandShake shake = new HandShake(this);
		logger.info("Requesting protocol version from " + serverAdress);
		int protocolVersion = shake.requestProtocolVersion();
		// we need to set up a new socket after protocol test handshaking as we want to properly connect now, which the
		// server wouldnt allow right away on the same connection
		reestablishConnection();
		logger.info("Sending handshake to " + serverAdress);
		shake.send(true, protocolVersion);
		// begin login
		logger.info("Beginning login to " + serverAdress);
		shake.sendLoginStartMessage(playerName);
		// figure out encryption secret
		logger.info("Parsing encryption request from " + serverAdress);
		EncryptionHandler asyncEncHandler = new EncryptionHandler(this);
		asyncEncHandler.parseEncryptionRequest();
		logger.info("Sending encryption reply to " + serverAdress);
		asyncEncHandler.sendEncryptionResponse();
		logger.info("Authenticating connection attempt to " + serverAdress + " against Yggdrassil session server");
		authHandler.authAgainstSessionServer(asyncEncHandler.generateKeyHash(), logger);
		// everything from here on is encrypted
		logger.info("Enabling sync encryption with " + serverAdress);
		encryptionEnabled = true;
		syncEncryptionHandler = new AES_CFB8_Encrypter(asyncEncHandler.getSharedSecret(), asyncEncHandler.getSharedSecret());
		GameJoinHandler joinHandler = new GameJoinHandler(this);
		joinHandler.parseLoginSuccess();
		// if we reach this point, we successfully logged in and the connection state switches to PLAY, so from now on
		// everything is handled by our standard packet handler
		logger.info("Switching connection to play state");
		playPacketHandler = new IncomingPlayPacketHandler(this);
		playerStatus = new PlayerStatus();
		new Thread(playPacketHandler).start();
		// still have to do this
		sendPacket(new ClientSettingPacket());

	}

	/**
	 * Sends a packet to the server. This method also handles both compression and encryption
	 * 
	 * @param packet
	 *          Packet to send
	 * @throws IOException
	 */
	public void sendPacket(WriteOnlyPacket packet) throws IOException {
		synchronized (output) {
			byte[] data;
			int packetSize = packet.getSize();
			if (compressionEnabled) {
				if (packetSize > maximumUncompressedPacketSize) {
					WriteOnlyPacket compressedPacket = new WriteOnlyPacket();
					byte[] compressedData = CompressionManager.compress(packet.toByteArray());
					// write length of uncompressed data
					compressedPacket.writeVarInt(packet.getSize());
					// write compressed data
					compressedPacket.writeBytes(compressedData);
					// standard append length of total packet at the front to finish compression
					data = compressedPacket.toByteArrayIncludingLength();
				} else {
					// no compression in which case we write 0 as uncompressed size and directly copy the data
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
			output.write(data);
		}
	}

	/**
	 * Gets a received packet if one is available and waits (non-blocking) until one is available if none is available.
	 * 
	 * @return Packet read
	 */
	public ReadOnlyPacket getPacket() throws IOException {
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
			byte[] decompressedData = CompressionManager.decompress(dataArray, logger);
			if (decompressedData.length != uncompressedPacketLength) {
				throw new IOException("Decompression failed and result in incorrect packet length");
			}
			return new ReadOnlyPacket(decompressedData);
		}
	}

	public boolean dataAvailable() {
		try {
			return input.available() != 0;
		} catch (IOException e) {
			logger.error("Failed to check for available data", e);
			return false;
		}
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
	 * @return Object containing all known information regarding the state of the player
	 */
	public PlayerStatus getPlayerStatus() {
		return playerStatus;
	}

	/**
	 * @return Handler for incoming packets while connection is in play state
	 */
	public IncomingPlayPacketHandler getIncomingPlayPacketHandler() {
		return playPacketHandler;
	}

	/**
	 * @return Logging instance used by this connection
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return Port used, 25565 is default
	 */
	public int getPort() {
		return port;
	}
}
