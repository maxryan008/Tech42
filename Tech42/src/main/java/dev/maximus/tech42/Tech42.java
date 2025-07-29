package dev.maximus.tech42;

import dev.maximus.tech42.gas.Tech42Gasses;
import net.fabricmc.api.ModInitializer;

public class Tech42 implements ModInitializer {

    @Override
    public void onInitialize() {
        Constant.LOGGER.info("Initializing Tech42 Main");
        Tech42Gasses.register();
    }
}
