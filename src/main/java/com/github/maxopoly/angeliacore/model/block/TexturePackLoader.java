package com.github.maxopoly.angeliacore.model.block;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.angeliacore.model.entity.AABB;
import com.github.maxopoly.angeliacore.util.FileUtil;

public class TexturePackLoader {

	private static final String client1_12_2_Url = "https://launcher.mojang.com/v1/objects/0f275bc1547d01fa5f56ba34bdc87d981ee12daf/client.jar";

	private Map<String, JSONObject> modelMap;
	private Logger logger;

	public TexturePackLoader(Logger logger) {
		this.logger = logger;
	}

	public void load(Logger logger, File f) throws ResourcePackParseException {
		if (!f.isDirectory()) {
			throw new ResourcePackParseException("Given path was not an existing directory");
		}
		File assets = new File(f, "assets");
		if (!assets.isDirectory()) {
			throw new ResourcePackParseException("assets/ folder not found");
		}
		File minecraft = new File(assets, "minecraft");
		if (!minecraft.isDirectory()) {
			throw new ResourcePackParseException("assets/minecraft/ folder not found");
		}
		File blockStates = new File(minecraft, "blockstates");
		if (!blockStates.isDirectory()) {
			throw new ResourcePackParseException("assets/minecraft/blockstates/ folder not found");
		}
		File models = new File(minecraft, "models");
		if (!models.isDirectory()) {
			throw new ResourcePackParseException("assets/minecraft/models/ folder not found");
		}
		File textures = new File(minecraft, "textures");
		if (!textures.isDirectory()) {
			throw new ResourcePackParseException("assets/minecraft/textures/ folder not found");
		}
		// Map<String, BufferedImage> textureMap = loadTextures("", textures, logger);
		// Map<String, JSONObject> blockStateMap = loadJSONFolder("", blockStates,
		// logger);
		modelMap = loadJSONFolder("", models, logger);
		modelMap = resolveModelDependencies(modelMap, logger);
	}

	public boolean ensureExistsAt(File f) {
		File assets = new File(f, "assets");
		if (!assets.isDirectory()) {
			f.delete();
			logger.info("No valid minecraft assets found, downloading from " + client1_12_2_Url);
			return FileUtil.downloadAndExtractZip(f.getAbsolutePath(), client1_12_2_Url, logger,
					Arrays.asList("assets"));
		}
		// TODO check hashes of entire folder etc.
		return true;
	}

	private Map<String, JSONObject> loadJSONFolder(String prefix, File folder, Logger logger)
			throws ResourcePackParseException {
		Map<String, JSONObject> map = new HashMap<>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				Map<String, JSONObject> subMap = loadJSONFolder(f.getName() + "/", f, logger);
				map.putAll(subMap);
				continue;
			}
			if (!f.getName().endsWith(".json")) {
				continue;
			}
			try {
				StringBuffer sb = new StringBuffer();
				for (String s : Files.readAllLines(f.toPath())) {
					sb.append(s);
				}
				JSONObject json = new JSONObject(sb.toString());
				map.put(prefix + reducePath(f.getName(), ".json"), json);
			} catch (IOException e) {
				logger.error("Failed to read json file", e);
				throw new ResourcePackParseException("Failed to read file " + f.getAbsolutePath());
			}
		}
		return map;
	}

	private Map<String, JSONObject> resolveModelDependencies(Map<String, JSONObject> map, Logger logger) {
		Map<String, JSONObject> result = new HashMap<>();
		for (Entry<String, JSONObject> entry : map.entrySet()) {
			JSONObject current = entry.getValue();
			while (current.has("parent")) {
				JSONObject parent = map.get(current.getString("parent"));
				if (parent == null) {
					// TODO TODO
					// logger.warn("Could not find parent " + current.getString("parent") + ".
					// Incomplete parent resolve for model: " + entry.getKey());
					break;
				}
				current.remove("parent");
				current = mergeJSON(parent, current);
			}
			result.put(entry.getKey(), current);
		}
		return result;
	}

	private static Map<String, BufferedImage> loadTextures(String prefix, File folder, Logger logger)
			throws ResourcePackParseException {
		Map<String, BufferedImage> result = new HashMap<>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				Map<String, BufferedImage> subMap = loadTextures(f.getName() + "/", f, logger);
				result.putAll(subMap);
				continue;
			}
			if (!f.getName().endsWith(".png")) {
				continue;
			}
			try {
				BufferedImage img = ImageIO.read(f);
				result.put(prefix + reducePath(f.getName(), ".png"), img);
			} catch (IOException e) {
				logger.error("Failed to read png file", e);
				throw new ResourcePackParseException("Failed to read file " + f.getAbsolutePath());
			}
		}
		return result;
	}

	private JSONObject mergeJSON(JSONObject parent, JSONObject child) {
		// dirty way to deep copy
		JSONObject result = new JSONObject(parent.toString());
		for (String key : child.keySet()) {
			if (!result.has(key)) {
				result.put(key, child.get(key));
				continue;
			}
			JSONObject optExistingValue = result.optJSONObject(key);
			if (optExistingValue == null) {
				// flat value which we will overwrite
				result.put(key, child.get(key));
				continue;
			}
			// jsons with same key exist, need to deep merge them
			result.put(key, mergeJSON(result.getJSONObject(key), child.getJSONObject(key)));
		}
		return result;

	}

	/**
	 * Removes .json from the end of a path
	 */
	private static String reducePath(String name, String extension) {
		if (!name.endsWith(extension)) {
			throw new IllegalArgumentException("Cannot remove " + extension + " from file name " + name);
		}
		return name.substring(0, name.length() - extension.length());
	}

	public AABB convertModelToAABB(String texturePackIdentifier) {
		JSONObject json = modelMap.get("block" + File.separator + texturePackIdentifier);
		if (json == null) {
			return null;
		}
		JSONArray elements = json.optJSONArray("elements");
		if (elements == null) {
			return null;
		}
		List<AABB> result = new ArrayList<>();
		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.optJSONObject(i);
			if (element == null) {
				logger.warn("Invalid entry " + elements.get(i) + " found in models for " + texturePackIdentifier
						+ ". Ignoring it");
				continue;
			}
			JSONArray fromArray = element.optJSONArray("from");
			JSONArray toArray = element.optJSONArray("to");
			if (fromArray == null || toArray == null || fromArray.length() != 3 || toArray.length() != 3) {
				logger.warn("Malformed model entry for " + texturePackIdentifier);
				continue;
			}
			result.add(new AABB(fromArray.getDouble(0) / 16, toArray.getDouble(0) / 16, fromArray.getDouble(1) / 16,
					toArray.getDouble(1) / 16, fromArray.getDouble(2) / 16, toArray.getDouble(2) / 16));
		}
		if (result.isEmpty()) {
			logger.warn("Could not load model entries for " + texturePackIdentifier);
			return null;
		}
		if (result.size() == 1) {
			return result.get(0);
		}
		return new AABB(result.toArray(new AABB[0]));
	}

}
