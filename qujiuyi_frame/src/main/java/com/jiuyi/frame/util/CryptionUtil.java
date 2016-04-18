package com.jiuyi.frame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptionUtil {

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	public static void main(String[] args) throws CryptoException {
		String password = "123456";
		File inputFile = new File("E:\\assets\\Jellyfish.jpg");
		encrypt(password, inputFile, new File("E:\\assets\\Jellyfish_backup.jpg"));
		decrypt(password, new File("E:\\assets\\Jellyfish_backup.jpg"), new File("E:\\assets\\Jellyfish1.jpg"));

	}

	public static void encrypt(String password, File inputFile, File outputFile) throws CryptoException {
		doCrypto(Cipher.ENCRYPT_MODE, password, inputFile, outputFile);
	}

	public static void decrypt(String password, File inputFile, File outputFile) throws CryptoException {
		doCrypto(Cipher.DECRYPT_MODE, password, inputFile, outputFile);
	}

	private static void doCrypto(int cipherMode, String password, File inputFile, File outputFile)
			throws CryptoException {
		try {
			String key = StringUtil.md5Str(password);
			Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			throw new CryptoException("Error encrypting/decrypting file", ex);
		}
	}

	static class CryptoException extends Exception {

		private static final long serialVersionUID = -5956485016315634554L;

		public CryptoException(String info, Exception ex) {
			super(info, ex);
		}

	}

}
