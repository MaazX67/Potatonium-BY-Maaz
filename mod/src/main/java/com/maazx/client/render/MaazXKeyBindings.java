package com.maazx.client.render;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public final class MaazXKeyBindings {
    public static KeyBinding TOGGLE_HUD;

    private MaazXKeyBindings() {
    }

    public static void register() {
        TOGGLE_HUD = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.maazxclient.toggle_hud",
                GLFW.GLFW_KEY_M,
                "category.maazxclient"
        ));
    }
}
