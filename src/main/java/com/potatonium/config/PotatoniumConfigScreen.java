package com.potatonium.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;

public class PotatoniumConfigScreen extends Screen {
	private final Screen parent;

	public PotatoniumConfigScreen(Screen parent) {
		super(Text.literal("Potatonium Config"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		// Add buttons for configuration
		this.addDrawableChild(ButtonWidget.builder(Text.literal("Potato Mode: " + (Potatonium.Potatonium.CONFIG.potatoMode ? "ON" : "OFF")),
			button -> {
				Potatonium.Potatonium.CONFIG.potatoMode = !Potatonium.Potatonium.CONFIG.potatoMode;
				Potatonium.Potatonium.CONFIG.save();
				button.setMessage(Text.literal("Potato Mode: " + (Potatonium.Potatonium.CONFIG.potatoMode ? "ON" : "OFF")));
			})
			.dimensions(this.width / 2 - 100, 40, 200, 20)
			.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("RAM Saver: " + (Potatonium.Potatonium.CONFIG.enableRamSaver ? "ON" : "OFF")),
			button -> {
				Potatonium.Potatonium.CONFIG.enableRamSaver = !Potatonium.Potatonium.CONFIG.enableRamSaver;
				Potatonium.Potatonium.CONFIG.save();
				button.setMessage(Text.literal("RAM Saver: " + (Potatonium.Potatonium.CONFIG.enableRamSaver ? "ON" : "OFF")));
			})
			.dimensions(this.width / 2 - 100, 70, 200, 20)
			.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("HDD Optimizer: " + (Potatonium.Potatonium.CONFIG.enableHDDOptimizer ? "ON" : "OFF")),
			button -> {
				Potatonium.Potatonium.CONFIG.enableHDDOptimizer = !Potatonium.Potatonium.CONFIG.enableHDDOptimizer;
				Potatonium.Potatonium.CONFIG.save();
				button.setMessage(Text.literal("HDD Optimizer: " + (Potatonium.Potatonium.CONFIG.enableHDDOptimizer ? "ON" : "OFF")));
			})
			.dimensions(this.width / 2 - 100, 100, 200, 20)
			.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Extreme Entity Culling: " + (Potatonium.Potatonium.CONFIG.enableExtremeEntityCulling ? "ON" : "OFF")),
			button -> {
				Potatonium.Potatonium.CONFIG.enableExtremeEntityCulling = !Potatonium.Potatonium.CONFIG.enableExtremeEntityCulling;
				Potatonium.Potatonium.CONFIG.save();
				button.setMessage(Text.literal("Extreme Entity Culling: " + (Potatonium.Potatonium.CONFIG.enableExtremeEntityCulling ? "ON" : "OFF")));
			})
			.dimensions(this.width / 2 - 100, 130, 200, 20)
			.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Potato HUD: " + (Potatonium.Potatonium.CONFIG.enablePotatoHUD ? "ON" : "OFF")),
			button -> {
				Potatonium.Potatonium.CONFIG.enablePotatoHUD = !Potatonium.Potatonium.CONFIG.enablePotatoHUD;
				Potatonium.Potatonium.CONFIG.save();
				button.setMessage(Text.literal("Potato HUD: " + (Potatonium.Potatonium.CONFIG.enablePotatoHUD ? "ON" : "OFF")));
			})
			.dimensions(this.width / 2 - 100, 160, 200, 20)
			.build());

		this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"),
			button -> this.client.setScreen(parent))
			.dimensions(this.width / 2 - 100, this.height - 30, 200, 20)
			.build());
	}

	@Override
	public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xffffff);
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
}
