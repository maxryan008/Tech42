package dev.maximus.tech42.mechanical;

import dev.maximus.tech42.Constant;
import dev.maximus.tech42.mechanical.gears.block_entities.WoodGearSmallEntity;
import dev.maximus.tech42.mechanical.gears.configs.WoodGearSmall;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;

public class Tech42Gears {
    public static void register() {
        TechcoreMechanicalPartRegistry.registerGear(
                Constant.id("wood_gear_small"),
                WoodGearSmall.class,
                WoodGearSmallEntity.class,
                Constant.id("block/wooden_gear")
        );
    }
}
