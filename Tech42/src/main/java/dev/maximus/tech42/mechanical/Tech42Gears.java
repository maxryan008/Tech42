package dev.maximus.tech42.mechanical;

import dev.maximus.tech42.Constant;
import dev.maximus.tech42.mechanical.gears.block_entities.IronGearSmallEntity;
import dev.maximus.tech42.mechanical.gears.block_entities.WoodGearSmallEntity;
import dev.maximus.tech42.mechanical.gears.configs.IronGearSmall;
import dev.maximus.tech42.mechanical.gears.configs.WoodGearSmall;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import net.minecraft.resources.ResourceLocation;

public class Tech42Gears {
    public static void register() {
        TechcoreMechanicalPartRegistry.registerGear(
                Constant.id("wood_gear_small"),
                WoodGearSmall.class,
                WoodGearSmallEntity.class,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/oak_planks")
        );
        TechcoreMechanicalPartRegistry.registerGear(
                Constant.id("iron_gear_small"),
                IronGearSmall.class,
                IronGearSmallEntity.class,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/iron_block")
        );
    }
}
