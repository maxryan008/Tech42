package dev.maximus.tech42.substance;

import dev.maximus.techcore.api.substance.CombustionRole;
import dev.maximus.techcore.api.substance.Substance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * Represents water (H₂O) as a substance, with properties across solid, liquid, and gas states.
 */
public class WaterSubstance extends Substance {
    public WaterSubstance(ResourceLocation id) {
        super(id);
    }

    @Override
    public float getMolecularWeight() {
        return 18.01528f; // H₂O
    }

    @Override
    public float getMeltingPoint() {
        return 273.15f; // 0 °C in Kelvin
    }

    @Override
    public float getBoilingPoint() {
        return 373.15f; // 100 °C in Kelvin
    }

    @Override
    public float getEnthalpyFusion() {
        return 6000f; // Approx 6 kJ/mol
    }

    @Override
    public float getEnthalpyVaporization() {
        return 40650f; // Approx 40.65 kJ/mol
    }

    @Override
    public float getDefaultPressure() {
        return 1.0f; // 1 atm
    }

    @Override
    public int getColor() {
        return 0x8844AAFF; // Semi-transparent blue
    }

    @Override
    public boolean isToxic() {
        return false;
    }

    @Override
    public CombustionRole getCombustionRole() {
        return CombustionRole.NONE; // Water is not combustible
    }

    @Override
    public Item getSolidForm() {
        return Items.ICE;
    }

    @Override
    public Fluid getLiquidForm() {
        return Fluids.WATER;
    }
}