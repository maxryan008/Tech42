package dev.maximus.techcore;

import dev.maximus.techcore.commands.TechcoreCommands;
import dev.maximus.techcore.mechanical.MechanicalNetworkManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;

public class Techcore implements ModInitializer {

    @Override
    public void onInitialize() {
        Constant.LOGGER.info("Initializing Techcore Main");
        CommandRegistrationCallback.EVENT.register(TechcoreCommands::register);

        // Register mechanical tick handler
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerLevel serverLevel) {
                MechanicalNetworkManager.get(serverLevel).tick(serverLevel);
            }
        });
    }
}
