package com.github.maxopoly.angeliacore.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.apache.logging.log4j.Logger;

/**
 * Handles standard gzip compression as used in packets
 *
 */
public class CompressionManager {

	/**
	 * Compresses the given data
	 * 
	 * @param data
	 *          Data to compress
	 * @return Compressed data
	 * @throws IOException
	 *           In case something goes wrong with the stream internally used
	 */
	public static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		return output;
	}

	/**
	 * Decompresses the given data
	 * 
	 * @param data
	 *          Data to decompress
	 * @param logger
	 *          Logger to use in case something goes wrong
	 * @return Decompressed data
	 * @throws IOException
	 *           If the dataformat is invalid
	 */
	public static byte[] decompress(byte[] data, Logger logger) throws IOException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
		} catch (DataFormatException e) {
			logger.error("Failed to decompress data", e);
			throw new IOException("Failed to decompress received data");
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		return output;

	}
}
