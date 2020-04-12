package com.github.maxopoly.angeliacore.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import org.apache.logging.log4j.Logger;

public final class FileUtil {

	private FileUtil() {
	}

	public static boolean downloadAndExtractZip(String extractionPath, String url, Logger logger, List<String> filter) {
		try (JarInputStream in = new JarInputStream(new URL(url).openStream())) {
			ZipEntry entry = in.getNextEntry();
			while (entry != null) {
				if (filter != null) {
					boolean hit = false;
					for (String preFix : filter) {
						if (entry.getName().startsWith(preFix)) {
							hit = true;
							break;
						}
					}
					if (!hit) {
						entry = in.getNextEntry();
						continue;
					}
				}
				String path = extractionPath + File.separator + entry.getName();
				if (entry.isDirectory()) {
					new File(path).mkdirs();
				} else {
					new File(path).getParentFile().mkdirs();
					Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
				}
				entry = in.getNextEntry();
			}
		} catch (IOException e) {
			logger.error("Failed to download and unpack zip file from " + url + " to " + extractionPath, e);
			return false;
		}
		return true;
	}

}
