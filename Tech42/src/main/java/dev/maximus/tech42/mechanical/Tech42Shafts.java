package dev.maximus.tech42.mechanical;

import dev.maximus.tech42.Constant;
import dev.maximus.tech42.mechanical.shafts.block_entities.IronShaftEntity;
import dev.maximus.tech42.mechanical.shafts.configs.IronShaft;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import net.minecraft.resources.ResourceLocation;

public class Tech42Shafts {
    public static void register() {
        TechcoreMechanicalPartRegistry.registerShaft(
                Constant.id("iron_shaft"),
                IronShaft.class,
                IronShaftEntity.class,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/iron_block")
        );
    }
}
