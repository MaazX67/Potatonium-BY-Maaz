package com.maazx.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class MaazXClientConfig {
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("maazxclient.properties");

    public static volatile boolean hudEnabled = true;
    public static volatile boolean entityCullingEnabled = true;
    public static volatile int entityCullDistance = 32;

    private MaazXClientConfig() {
    }

    public static synchronized void load() {
        if (!Files.exists(PATH)) {
            save();
            return;
        }

        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(PATH)) {
            properties.load(reader);
        } catch (IOException exception) {
            MaazXClient.LOGGER.error("Could not read {}.", PATH, exception);
            return;
        }

        hudEnabled = readBoolean(properties, "hud_enabled", hudEnabled);
        entityCullingEnabled = readBoolean(properties, "entity_culling_enabled", entityCullingEnabled);
        entityCullDistance = readDistance(properties, "entity_cull_distance", entityCullDistance);
    }

    public static synchronized void save() {
        Properties properties = new Properties();
        properties.setProperty("hud_enabled", Boolean.toString(hudEnabled));
        properties.setProperty("entity_culling_enabled", Boolean.toString(entityCullingEnabled));
        properties.setProperty("entity_cull_distance", Integer.toString(entityCullDistance));

        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                properties.store(writer, "MaazXClient settings");
            }
        } catch (IOException exception) {
            MaazXClient.LOGGER.error("Could not save {}.", PATH, exception);
        }
    }

    private static boolean readBoolean(Properties properties, String key, boolean fallback) {
        String value = properties.getProperty(key);
        if (value == null) {
            return fallback;
        }
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }

        MaazXClient.LOGGER.warn("Ignoring invalid boolean value for {} in {}.", key, PATH);
        return fallback;
    }

    private static int readDistance(Properties properties, String key, int fallback) {
        String value = properties.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            int distance = Integer.parseInt(value);
            if (distance >= 16 && distance <= 128) {
                return distance;
            }
        } catch (NumberFormatException exception) {
            MaazXClient.LOGGER.warn("Ignoring invalid distance value for {} in {}.", key, PATH);
            return fallback;
        }

        MaazXClient.LOGGER.warn("Ignoring out-of-range distance value for {} in {}.", key, PATH);
        return fallback;
    }
}
