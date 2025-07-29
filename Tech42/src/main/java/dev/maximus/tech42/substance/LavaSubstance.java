package dev.maximus.tech42.substance;

import dev.maximus.techcore.api.substance.CombustionRole;
import dev.maximus.techcore.api.substance.Substance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class LavaSubstance extends Substance {
    public LavaSubstance(ResourceLocation id) {
        super(id);
    }

    @Override
    public float getMolecularWeight() {
        return 60.08f; // SiO₂
    }

    @Override
    public float getMeltingPoint() {
        return 1373.15f; // 1710°C
    }

    @Override
    public float getBoilingPoint() {
        return 3223.15f; // 2950°C
    }

    @Override
    public float getEnthalpyFusion() {
        return 850_000f; // J/mol
    }

    @Override
    public float getEnthalpyVaporization() {
        return 9_100_000f; // J/mol
    }

    @Override
    public float getDefaultPressure() {
        return 1.0f;
    }

    @Override
    public int getColor() {
        return 0xFFA0522D; // Lava-like orange-brown
    }

    @Override
    public boolean isToxic() {
        return false;
    }

    @Override
    public CombustionRole getCombustionRole() {
        return CombustionRole.NONE;
    }

    @Override
    public Item getSolidForm() {
        return Items.STONE;
    }

    @Override
    public Fluid getLiquidForm() {
        return Fluids.LAVA;
    }
}
