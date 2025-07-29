package dev.maximus.tech42.client;

import dev.maximus.tech42.Constant;
import net.fabricmc.api.ClientModInitializer;

public class Tech42Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Constant.LOGGER.info("Initializing Tech42 Client");
    }
}
