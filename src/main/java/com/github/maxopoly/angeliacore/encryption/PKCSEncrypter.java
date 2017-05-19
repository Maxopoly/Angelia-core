package com.github.maxopoly.angeliacore.encryption;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PKCSEncrypter {

	// modified version of code at http://www.java2s.com/Tutorial/Java/0490__Security/RSAexamplewithPKCS1Padding.htm

	public static byte[] encrypt(byte[] input, Key pubKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			SecureRandom random = new SecureRandom();
			cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
			byte[] cipherText = cipher.doFinal(input);
			return cipherText;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
