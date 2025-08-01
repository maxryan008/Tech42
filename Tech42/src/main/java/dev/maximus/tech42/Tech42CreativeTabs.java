package dev.maximus.tech42;

import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class Tech42CreativeTabs {
    public static final CreativeModeTab TECH42_TAB = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.tech42.tab"))
            .icon(() -> new ItemStack(getItem("wood_gear_small")))
            .displayItems((params, output) -> {
                output.accept(getItem("wood_gear_small"));
                output.accept(getItem("iron_gear_small"));
            })
            .build();

    public static final Block getItem(String id) {
        return TechcoreMechanicalPartRegistry.getRegisteredGearBlock(Constant.id(id));
    }

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                Constant.id("main"),
                Tech42CreativeTabs.TECH42_TAB);
    }
}