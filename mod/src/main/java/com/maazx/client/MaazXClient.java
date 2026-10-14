package com.maazx.client;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MaazXClient implements ModInitializer {
    public static final String MOD_ID = "maazxclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MaazXClientConfig.load();
        LOGGER.info("MaazXClient initialized.");
    }
}
