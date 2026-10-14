package com.maazx.client.render;

import com.maazx.client.MaazXClientConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class FpsHud {
    private FpsHud() {
    }

    public static void render(DrawContext context, float tickDelta) {
        if (!MaazXClientConfig.hudEnabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) {
            return;
        }

        Runtime runtime = Runtime.getRuntime();
        long usedMiB = (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
        String status = "MaazX  " + client.getCurrentFps() + " FPS  |  " + usedMiB + " MiB";
        int x = client.getWindow().getScaledWidth() - client.textRenderer.getWidth(status) - 6;
        context.drawTextWithShadow(client.textRenderer, status, x, 6, 0x55F2FF);
    }
}
