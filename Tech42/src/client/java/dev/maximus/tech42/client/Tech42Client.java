package dev.maximus.tech42.client;

import net.fabricmc.api.ClientModInitializer;

public class Tech42Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("Initializing Tech42Client");
    }
}
