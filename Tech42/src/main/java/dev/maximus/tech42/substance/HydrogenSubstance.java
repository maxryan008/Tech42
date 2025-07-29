package dev.maximus.tech42.substance;

import dev.maximus.techcore.api.substance.CombustionRole;
import dev.maximus.techcore.api.substance.Substance;
import net.minecraft.resources.ResourceLocation;

public class HydrogenSubstance extends Substance {
    public HydrogenSubstance(ResourceLocation id) {
        super(id);
    }

    @Override
    public float getMolecularWeight() {
        return 2.016f; // H₂: 1.008 * 2
    }

    @Override
    public float getMeltingPoint() {
        return 13.99f; // Kelvin (-259.16 °C)
    }

    @Override
    public float getBoilingPoint() {
        return 20.27f; // Kelvin (-252.88 °C)
    }

    @Override
    public float getEnthalpyFusion() {
        return 117f; // J/mol (approx. for H₂)
    }

    @Override
    public float getEnthalpyVaporization() {
        return 904f; // J/mol (approx. for H₂)
    }

    @Override
    public float getDefaultPressure() {
        return 1.0f; // atm
    }

    @Override
    public int getColor() {
        return 0xFFFFFFAA; // Pale yellow-white with some transparency
    }

    @Override
    public boolean isToxic() {
        return false; // Non-toxic
    }

    @Override
    public CombustionRole getCombustionRole() {
        return CombustionRole.FUEL;
    }
}