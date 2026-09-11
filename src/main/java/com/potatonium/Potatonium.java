package com.potatonium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.FabricClientEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.potatonium.config.PotatoniumConfig;
import com.potatonium.detection.PotatoDetectionEngine;
import com.potatonium.optimization.PerformanceManager;

public class Potatonium implements ModInitializer {
	public static final String MOD_ID = "potatonium";
	public static final Logger LOGGER = LoggerFactory.getLogger("Potatonium");

	public static PotatoniumConfig CONFIG;
	public static PotatoDetectionEngine DETECTION_ENGINE;
	public static PerformanceManager PERFORMANCE_MANAGER;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Potatonium - The Ultimate Potato PC Performance Mod");

		// Load configuration
		CONFIG = new PotatoniumConfig();
		CONFIG.load();

		// Initialize potato detection engine
		DETECTION_ENGINE = new PotatoDetectionEngine();
		DETECTION_ENGINE.detectHardware();

		// Initialize performance manager
		PERFORMANCE_MANAGER = new PerformanceManager();

		LOGGER.info("Potatonium initialized successfully!");
		LOGGER.info("Potato Mode: {}", CONFIG.potatoMode);
		LOGGER.info("System RAM: {}GB", DETECTION_ENGINE.getTotalRamGB());
		LOGGER.info("CPU Throttle: {}MHz", DETECTION_ENGINE.getCurrentCPUMHz());
	}
}
