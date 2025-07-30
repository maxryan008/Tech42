package dev.maximus.tech42.pipe;

import dev.maximus.techcore.api.pipe.TechcorePipes;
import net.minecraft.world.level.block.Blocks;

public class Tech42Pipes {
    public static void register() {
        TechcorePipes.registerPipe(Blocks.COPPER_BLOCK, new CopperPipeType());
    }
}