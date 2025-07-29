package dev.maximus.tech42.gas;

import dev.maximus.tech42.Constant;
import dev.maximus.techcore.api.gas.GasRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers Tech42-specific gases to the global gas registry.
 */
public class Tech42Gasses {
    public static final ResourceLocation OXYGEN_ID = Constant.id("oxygen");

    public static void register() {
        GasRegistry.register(OXYGEN_ID, new OxygenGas(OXYGEN_ID, 32.00f)); // O₂ = 2×16.00 g/mol
    }
}