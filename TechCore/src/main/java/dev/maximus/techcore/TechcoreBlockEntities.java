package dev.maximus.techcore;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TechcoreBlockEntities {
    public static final TechcoreRegistry<BlockEntityType<?>> BLOCK_ENTITIES = new TechcoreRegistry<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);
}
