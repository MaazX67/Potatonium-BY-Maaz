package com.maazx.client.ui;

import com.maazx.client.MaazXClientConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class ClientConfigScreen extends Screen {
    private final Screen parent;

    public ClientConfigScreen(Screen parent) {
        super(Text.literal("MaazXClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 100;
        int top = this.height / 2 - 48;

        addDrawableChild(ButtonWidget.builder(hudLabel(), button -> {
            MaazXClientConfig.hudEnabled = !MaazXClientConfig.hudEnabled;
            MaazXClientConfig.save();
            button.setMessage(hudLabel());
        }).dimensions(left, top, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(cullingLabel(), button -> {
            MaazXClientConfig.entityCullingEnabled = !MaazXClientConfig.entityCullingEnabled;
            MaazXClientConfig.save();
            button.setMessage(cullingLabel());
        }).dimensions(left, top + 25, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(distanceLabel(), button -> {
            MaazXClientConfig.entityCullDistance =
                    MaazXClientConfig.entityCullDistance >= 128 ? 16 : MaazXClientConfig.entityCullDistance + 16;
            MaazXClientConfig.save();
            button.setMessage(distanceLabel());
        }).dimensions(left, top + 50, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button -> close())
                .dimensions(left, top + 82, 200, 20)
                .build());
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    private static Text hudLabel() {
        return Text.literal("FPS HUD: " + enabledText(MaazXClientConfig.hudEnabled));
    }

    private static Text cullingLabel() {
        return Text.literal("Entity render culling: " + enabledText(MaazXClientConfig.entityCullingEnabled));
    }

    private static Text distanceLabel() {
        return Text.literal("Entity range: " + MaazXClientConfig.entityCullDistance + " blocks");
    }

    private static String enabledText(boolean enabled) {
        return enabled ? "On" : "Off";
    }
}
