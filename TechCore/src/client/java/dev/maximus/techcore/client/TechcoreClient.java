package dev.maximus.techcore.client;

import dev.maximus.techcore.Constant;
import net.fabricmc.api.ClientModInitializer;

public class TechcoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Constant.LOGGER.info("Initializing Techcore Client");
    }
}
