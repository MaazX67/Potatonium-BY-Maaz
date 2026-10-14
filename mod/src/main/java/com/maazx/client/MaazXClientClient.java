package com.maazx.client;

import com.maazx.client.render.FpsHud;
import com.maazx.client.render.MaazXKeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class MaazXClientClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MaazXKeyBindings.register();
        HudRenderCallback.EVENT.register(FpsHud::render);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MaazXKeyBindings.TOGGLE_HUD.wasPressed()) {
                if (!client.options.debugEnabled) {
                    continue;
                }

                MaazXClientConfig.hudEnabled = !MaazXClientConfig.hudEnabled;
                MaazXClientConfig.save();
            }
        });
    }
}
