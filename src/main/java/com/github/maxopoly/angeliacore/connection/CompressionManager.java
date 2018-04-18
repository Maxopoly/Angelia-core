package com.github.maxopoly.angeliacore.connection;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import com.github.maxopoly.angeliacore.exceptions.MalformedCompressedDataException;

/**
 * Handles standard gzip compression as used in packets
 *
 */
public class CompressionManager {

	/**
	 * Compresses the given data with zlib (as used by minecrafts protocol)
	 * 
	 * @param data
	 *            Data to compress
	 * @return Compressed data
	 * @throws IOException
	 *             In case something goes wrong with the stream internally used
	 */
	public static byte[] compressZLib(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
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
	 * Decompresses the given data with zlib
	 * 
	 * @param data
	 *            Data to decompress
	 * @param logger
	 *            Logger to use in case something goes wrong
	 * @return Decompressed data
	 * @throws IOException
	 *             If the dataformat is invalid
	 * @throws MalformedCompressedDataException
	 */
	public static byte[] decompressZLib(byte[] data, Logger logger)
			throws MalformedCompressedDataException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
		} catch (DataFormatException e) {
			if (logger != null) {
				logger.error("Failed to decompress data", e);
			}
			throw new MalformedCompressedDataException(
					"Could not decompress with ZLib");
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			// should never happen
			e.printStackTrace();
		}
		byte[] output = outputStream.toByteArray();
		return output;

	}

	public static byte[] compressGZip(final byte[] data) throws IOException {
		if (data == null) {
			return null;
		}
		if (data.length == 0) {
			return new byte[0];
		}
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(obj);
		gzip.write(data);
		gzip.flush();
		gzip.close();
		return obj.toByteArray();
	}

	public static byte[] decompressGZip(final byte[] compressed)
			throws MalformedCompressedDataException {
		if (compressed == null) {
			return null;
		}
		if (compressed.length == 0) {
			return new byte[0];
		}
		if (isGZipCompressed(compressed)) {
			try {
				GZIPInputStream gis = new GZIPInputStream(
						new ByteArrayInputStream(compressed));
				return IOUtils.toByteArray(gis);
			} catch (IOException e) {
				e.printStackTrace();
				throw new MalformedCompressedDataException(
						"Could not decompress with GZIP");
			}

		} else {
			throw new MalformedCompressedDataException(
					"Could not decompress with GZIP");
		}
	}

	private static boolean isGZipCompressed(final byte[] compressed) {
		return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
				&& (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
	}

}
