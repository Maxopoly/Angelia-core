package com.github.maxopoly.MineApp.connection.login;

import com.github.maxopoly.MineApp.connection.ServerConnection;
import com.github.maxopoly.MineApp.encryption.PKCSEncrypter;
import com.github.maxopoly.MineApp.packet.ReadOnlyPacket;
import com.github.maxopoly.MineApp.packet.WriteOnlyPacket;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionHandler {

	private ServerConnection connection;

	private Key serverPubKey;
	private byte[] encodedPubKey;
	private byte[] serverVerifyToken;
	private String serverID;

	private byte[] sharedSecret;

	public EncryptionHandler(ServerConnection connection) {
		this.connection = connection;
	}

	public byte[] getSharedSecret() {
		return sharedSecret;
	}

	public void parseEncryptionRequest() throws IOException {
		try {
			ReadOnlyPacket packet = connection.getPacket();
			byte packetID = packet.getPacketID();
			assert (packetID == (byte) 0x01);
			serverID = packet.readString();
			encodedPubKey = packet.readByteArray();
			X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(encodedPubKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			serverPubKey = kf.generatePublic(X509publicKey);
			serverVerifyToken = packet.readByteArray();
		} catch (Exception e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Failed to parse encryption request - " + e.getClass());
		}
	}

	public void sendEncryptionResponse() throws IOException {
		WriteOnlyPacket encPacket = new WriteOnlyPacket(0x01);
		SecureRandom rng = new SecureRandom();
		sharedSecret = new byte[16];
		rng.nextBytes(sharedSecret); // gen random secret
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

	public String generateKeyHash() throws IOException {
		try {
			String mc = new BigInteger(digestOperation("SHA-1", new byte[][] { serverID.getBytes("ISO_8859_1"), sharedSecret,
					encodedPubKey })).toString(16);
			return mc;
		} catch (Exception e) {
			connection.getLogger().error("Exception occured", e);
			throw new IOException("Failed to gen encryption hash");
		}
	}

	private static byte[] digestOperation(String algorithm, byte[]... data) {
		try {
			MessageDigest messagedigest = MessageDigest.getInstance(algorithm);

			for (byte[] abyte : data) {
				messagedigest.update(abyte);
			}

			return messagedigest.digest();
		} catch (NoSuchAlgorithmException nosuchalgorithmexception) {
			nosuchalgorithmexception.printStackTrace();
			return null;
		}
	}

}
