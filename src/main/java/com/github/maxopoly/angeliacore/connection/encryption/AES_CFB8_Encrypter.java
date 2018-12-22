package com.github.maxopoly.angeliacore.connection.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_CFB8_Encrypter {

	private Cipher decodingCipher;
	private Cipher encodingCipher;

	private SecretKey keySpec;
	private IvParameterSpec ivSpec;

	public AES_CFB8_Encrypter(byte[] secretKey, byte[] initialVector) {
		keySpec = new SecretKeySpec(secretKey, "AES");
		ivSpec = new IvParameterSpec(initialVector);
		try {
			decodingCipher = Cipher.getInstance("AES/CFB8/NoPadding");
			decodingCipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			encodingCipher = Cipher.getInstance("AES/CFB8/NoPadding");
			encodingCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e);
		} catch (NoSuchPaddingException e) {
			throw new SecurityException(e);
		}
	}

	public byte[] decrypt(byte[] input) {
		synchronized (decodingCipher) {
			return decodingCipher.update(input);
		}
	}

	public byte[] encrypt(byte[] input) {
		synchronized (encodingCipher) {
			return encodingCipher.update(input);
		}
	}
}
