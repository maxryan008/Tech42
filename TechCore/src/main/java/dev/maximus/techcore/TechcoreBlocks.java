package dev.maximus.techcore;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class TechcoreBlocks {
    public static void registerBlock(ResourceLocation id, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, id, block);
    }
}
