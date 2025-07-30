package dev.maximus.tech42;

import dev.maximus.tech42.mechanical.Tech42Gears;
import dev.maximus.tech42.mechanical.Tech42Shafts;
import dev.maximus.tech42.pipe.Tech42Pipes;
import dev.maximus.tech42.substance.Tech42Substances;
import net.fabricmc.api.ModInitializer;

public class Tech42 implements ModInitializer {

    @Override
    public void onInitialize() {
        Constant.LOGGER.info("Initializing Tech42 Main");
        Tech42Substances.register();
        Tech42Pipes.register();
        Tech42Gears.register();
        Tech42Shafts.register();
        Tech42CreativeTabs.register();
    }
}
