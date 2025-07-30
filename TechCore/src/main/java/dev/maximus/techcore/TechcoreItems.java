package dev.maximus.techcore;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TechcoreItems {
    public static void registerBlockItem(ResourceLocation id, Block block) {
        Item item = new BlockItem(block, new Item.Properties());
        Registry.register(BuiltInRegistries.ITEM, id, item);
    }
}
