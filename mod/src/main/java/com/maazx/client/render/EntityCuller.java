package com.maazx.client.render;

import com.maazx.client.MaazXClientConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class EntityCuller {
    private EntityCuller() {
    }

    public static boolean shouldRender(Entity entity) {
        if (!MaazXClientConfig.entityCullingEnabled || entity instanceof PlayerEntity) {
            return true;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) {
            return true;
        }

        Vec3d cameraPosition = client.gameRenderer.getCamera().getPos();
        double distance = MaazXClientConfig.entityCullDistance;
        return entity.squaredDistanceTo(cameraPosition) <= distance * distance;
    }
}
