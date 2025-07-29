package dev.maximus.tech42.substance;

import dev.maximus.tech42.Constant;
import dev.maximus.techcore.api.substance.SubstanceRegistry;
import net.minecraft.resources.ResourceLocation;

public class Tech42Substances {
    public static final ResourceLocation OXYGEN_ID = Constant.id("oxygen");
    public static final ResourceLocation HYDROGEN_ID = Constant.id("hydrogen");

    public static void register() {
        SubstanceRegistry.register(OXYGEN_ID, new OxygenSubstance(OXYGEN_ID));
        SubstanceRegistry.register(HYDROGEN_ID, new HydrogenSubstance(HYDROGEN_ID));
    }
}