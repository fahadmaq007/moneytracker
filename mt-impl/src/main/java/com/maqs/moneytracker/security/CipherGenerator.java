package com.maqs.moneytracker.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.maqs.moneytracker.common.util.StringUtil;

public class CipherGenerator {

	private KeyGenerator keyGen = null;

	private SecretKey secretKey = null;

	private Cipher aesCipher = null;

	private byte[] salt = "FqrgltC5KAyYPeGZ".getBytes();

	private static final String AES = "AES";

	private final String algo;

	public CipherGenerator() {
		this(AES);
	}

	public CipherGenerator(String algo) {
		try {
			this.algo = algo;
			secretKey = new SecretKeySpec(salt, 0, salt.length, algo);
			keyGen = KeyGenerator.getInstance(algo);
			aesCipher = Cipher.getInstance(algo);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
		keyGen.init(128);
	}

	public byte[] encrypt(String plainData) throws Exception {
		aesCipher = Cipher.getInstance(algo);
		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] byteDataToEncrypt = plainData.getBytes();
		byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt);

		return byteCipherText;
	}

	public String decrypt(byte[] encryptedCipher) throws Exception {

		aesCipher.init(Cipher.DECRYPT_MODE, secretKey,
				aesCipher.getParameters());
		byte[] byteDecryptedText = aesCipher.doFinal(encryptedCipher);

		String decryptedText = new String(byteDecryptedText);
		return decryptedText;
	}

	public static void main(String[] args) {
		try {
			new CipherGenerator().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() throws Exception {
		String text = "admin";
		String enc = encryptToBase64(text);
		System.out.println("EnC: " + enc);

		System.out.println("Dec: " + decrypt(Base64.decodeBase64(enc)));
	}

	public void setSalt(String saltText) {
		if (! StringUtil.nullOrEmpty(saltText)) {
			salt = saltText.getBytes();
		}
	}

	public String encryptToBase64(String text) throws Exception {
		return Base64.encodeBase64String(encrypt(text));
	}
	
}
