package dev.maximus.techcore.client;

import net.fabricmc.api.ClientModInitializer;

public class TechcoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("Initializing TechcoreClient");
    }
}
