package dev.maximus.techcore;

import dev.maximus.techcore.commands.TechcoreCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Techcore implements ModInitializer {

    @Override
    public void onInitialize() {
        Constant.LOGGER.info("Initializing Techcore Main");
        CommandRegistrationCallback.EVENT.register(TechcoreCommands::register);
    }
}
