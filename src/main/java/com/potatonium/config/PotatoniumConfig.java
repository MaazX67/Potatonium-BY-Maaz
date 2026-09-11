package com.potatonium.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PotatoniumConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String CONFIG_FILE = "config/potatonium.json";

	// Potato Mode
	public boolean potatoMode = true;
	public int renderDistance = 4;
	public int simulationDistance = 3;
	public boolean disableFancy = true;

	// RAM Saver
	public boolean enableRamSaver = true;
	public int ramSaverThreshold = 85; // %
	public boolean enableChunkNBTCompression = true;
	public boolean enableObjectPooling = true;

	// HDD Optimizer
	public boolean enableHDDOptimizer = true;
	public int chunkCacheSize = 100;
	public int autoSaveInterval = 45; // seconds
	public boolean enableAsyncChunkLoading = true;

	// Software Renderer
	public boolean enableSoftwareRenderBoost = true;
	public boolean forceSoftwareGL = false;
	public boolean enableSwiftShaderFallback = true;

	// Render Culling
	public boolean enableExtremeEntityCulling = true;
	public int entityCullingDistance = 32;
	public boolean disableEntityShadows = true;
	public boolean disableClouds = true;
	public boolean disableFog = false; // May affect gameplay
	public int particleCullingPercent = 90;

	// CPU Unlocker
	public boolean enableCPUUnlocker = true;
	public boolean enableHighPerformanceTimer = true;
	public boolean disableCPUParking = true;
	public int maxThreads = 4;

	// Fun Potato Features
	public boolean enablePotatoHUD = true;
	public boolean enablePotatoCounter = true;
	public boolean enableBatteryWarning = true;
	public boolean enableAluMode = true;
	public int aluModeThreshold = 15; // FPS
	public boolean enablePotatoniumToast = true;

	public void load() {
		try {
			File configFile = new File(CONFIG_FILE);
			if (configFile.exists()) {
				PotatoniumConfig loaded = GSON.fromJson(new FileReader(configFile), PotatoniumConfig.class);
				copyFromLoaded(loaded);
				Potatonium.Potatonium.LOGGER.info("Loaded potatonium config");
			} else {
				save();
			}
		} catch (IOException e) {
			Potatonium.Potatonium.LOGGER.error("Failed to load config", e);
		}
	}

	public void save() {
		try {
			File configDir = new File("config");
			if (!configDir.exists()) {
				configDir.mkdirs();
			}
			FileWriter writer = new FileWriter(CONFIG_FILE);
			GSON.toJson(this, writer);
			writer.close();
			Potatonium.Potatonium.LOGGER.info("Saved potatonium config");
		} catch (IOException e) {
			Potatonium.Potatonium.LOGGER.error("Failed to save config", e);
		}
	}

	private void copyFromLoaded(PotatoniumConfig loaded) {
		this.potatoMode = loaded.potatoMode;
		this.renderDistance = loaded.renderDistance;
		this.simulationDistance = loaded.simulationDistance;
		this.disableFancy = loaded.disableFancy;
		this.enableRamSaver = loaded.enableRamSaver;
		this.ramSaverThreshold = loaded.ramSaverThreshold;
		this.enableHDDOptimizer = loaded.enableHDDOptimizer;
		this.chunkCacheSize = loaded.chunkCacheSize;
		this.autoSaveInterval = loaded.autoSaveInterval;
		this.enableSoftwareRenderBoost = loaded.enableSoftwareRenderBoost;
		this.enableExtremeEntityCulling = loaded.enableExtremeEntityCulling;
		this.enablePotatoHUD = loaded.enablePotatoHUD;
		this.enablePotatoCounter = loaded.enablePotatoCounter;
		this.enableBatteryWarning = loaded.enableBatteryWarning;
	}
}
