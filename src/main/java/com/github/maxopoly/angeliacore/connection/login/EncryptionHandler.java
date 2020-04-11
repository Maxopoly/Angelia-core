package com.github.maxopoly.angeliacore.connection.login;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.encryption.PKCSEncrypter;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class EncryptionHandler {

	/**
	 * Passes the data through the given algorithm
	 * 
	 * @param algorithm - The algorithm
	 * @param data      - The data to pass through
	 * @return The result of the operation
	 */
	private byte[] digestOperation(String algorithm, byte[]... data) {
		try {
			/* no need to make it static, since it's only used once, and it hogs memory */
			MessageDigest messagedigest = MessageDigest.getInstance(algorithm);

			for (byte[] abyte : data) {
				messagedigest.update(abyte);
			}

			return messagedigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ServerConnection connection;
	private Key serverPubKey;
	private byte[] encodedPubKey;
	private byte[] serverVerifyToken;

	private String serverID;

	private byte[] sharedSecret;

	public EncryptionHandler(ServerConnection connection) {
		this.connection = connection;
	}

	/**
	 * Generates the SHA-1 hash of the key
	 * 
	 * @return The hash needed
	 * @throws IOException If something goes wrong
	 */
	public String generateKeyHash() throws IOException {
		try {
			String mc = new BigInteger(digestOperation("SHA-1",
					new byte[][] { serverID.getBytes("ISO_8859_1"), sharedSecret, encodedPubKey })).toString(16);
			return mc;
		} catch (Exception e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Failed to gen encryption hash");
		}
	}

	/**
	 * Creates a secure random key
	 */
	public void genSecretKey() {
		SecureRandom rng = new SecureRandom();
		sharedSecret = new byte[16];
		rng.nextBytes(sharedSecret); // gen random secret
	}

	public byte[] getSharedSecret() {
		return sharedSecret;
	}

	/**
	 * Parses the encryption request
	 * 
	 * @param packet - The packet received
	 * @return If the request was parsed successfully
	 * @throws IOException - If it didn't work
	 */
	public boolean parseEncryptionRequest(ReadOnlyPacket packet) throws IOException {
		try {
			byte packetID = packet.getPacketID();
			if (packetID == 0x00) {
				new GameJoinHandler(connection).handleDisconnectPacket(packet);
				return false;
			}
			if (packetID != 0x01) {
				connection.getLogger()
						.error("Expected encryption request packet, " + "but received packed with id " + packetID);
				return false;
			}
			serverID = packet.readString();
			encodedPubKey = packet.readByteArray();
			X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(encodedPubKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			serverPubKey = kf.generatePublic(X509publicKey);
			serverVerifyToken = packet.readByteArray();
			return true;
		} catch (Exception e) {
			connection.getLogger().error("Failed to parse encryption request - ", e);
			return false;
		}
	}

	/**
	 * Parses the encryption request from the next packet available on the
	 * connection
	 * 
	 * @return {@link EncryptionHandler#parseEncryptionRequest(ReadOnlyPacket)}
	 * @throws IOException {@link EncryptionHandler#parseEncryptionRequest(ReadOnlyPacket)}
	 */
	public boolean parseEncryptionRequest() throws IOException {
		try {
			ReadOnlyPacket packet = connection.getPacket();
			return parseEncryptionRequest(packet);
		} catch (Exception e) {
			connection.getLogger().error("Failed to parse encryption request - ", e);
			return false;
		}
	}

	/**
	 * Sends the encryption response, confirming everything went fine
	 * 
	 * @throws IOException - If something goes wrong
	 */
	public void sendEncryptionResponse() throws IOException {
		WriteOnlyPacket encPacket = new WriteOnlyPacket(0x01);
		byte[] encryptedSharedSecret = PKCSEncrypter.encrypt(sharedSecret, serverPubKey);
		byte[] encryptedVerifyToken = PKCSEncrypter.encrypt(serverVerifyToken, serverPubKey);
		try {
			// these should always be true due to the encryption padding
			assert encryptedSharedSecret.length == 128;
			assert encryptedVerifyToken.length == 128;
			encPacket.writeByteArray(encryptedSharedSecret);
			encPacket.writeByteArray(encryptedVerifyToken);
			connection.sendPacket(encPacket);
		} catch (Exception e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Failed to send encryption response - " + e.getClass());
		}
	}

}
