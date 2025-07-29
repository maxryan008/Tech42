package dev.maximus.tech42.substance;

import dev.maximus.techcore.api.substance.CombustionRole;
import dev.maximus.techcore.api.substance.Substance;
import net.minecraft.resources.ResourceLocation;

public class OxygenSubstance extends Substance {
    public OxygenSubstance(ResourceLocation id) {
        super(id);
    }

    @Override
    public float getMolecularWeight() {
        return 32.00f; // O₂: 16.00 * 2
    }

    @Override
    public float getMeltingPoint() {
        return 54.36f; // Kelvin (-218.79 °C)
    }

    @Override
    public float getBoilingPoint() {
        return 90.20f; // Kelvin (-182.95 °C)
    }

    @Override
    public float getEnthalpyVaporization() {
        return 6820f; // J/mol at 1 atm (approx. for O₂)
    }

    @Override
    public float getEnthalpyFusion() {
        return 1380f; // J/mol (approx. for O₂)
    }

    @Override
    public float getDefaultPressure() {
        return 1.0f; // atm
    }

    @Override
    public int getColor() {
        return 0x66CCFFFF; // Light blue, full alpha
    }

    @Override
    public boolean isToxic() {
        return false; // Safe under normal conditions
    }

    @Override
    public CombustionRole getCombustionRole() {
        return CombustionRole.OXYGENATOR;
    }
}