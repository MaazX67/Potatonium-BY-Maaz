package com.potatonium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.FabricClientEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.potatonium.hud.PotatoniumHUD;
import com.potatonium.optimization.cpu.BatteryThrottlingFix;
import com.potatonium.optimization.render.SoftwareRenderBoost;

public class PotatoniumClient implements ClientModInitializer {
	public static PotatoniumHUD HUD;

	@Override
	public void onInitializeClient() {
		Potatonium.LOGGER.info("Initializing Potatonium Client");

		// Initialize HUD
		HUD = new PotatoniumHUD();

		// Apply battery throttling fix
		if (Potatonium.DETECTION_ENGINE.isBatteryThrottled()) {
			BatteryThrottlingFix.applyFix();
			Potatonium.LOGGER.info("Applied battery throttling fix");
		}

		// Apply software render boost if needed
		if (Potatonium.DETECTION_ENGINE.isLowEndGPU()) {
			SoftwareRenderBoost.initialize();
			Potatonium.LOGGER.info("Software render boost initialized");
		}

		// Register client tick events
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (Potatonium.CONFIG.enablePotatoHUD && client.screen == null) {
				HUD.update();
			}
		});

		Potatonium.LOGGER.info("Potatonium Client initialized");
	}
}
