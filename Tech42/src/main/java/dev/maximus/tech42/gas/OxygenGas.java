package dev.maximus.tech42.gas;

import dev.maximus.techcore.api.gas.Gas;
import dev.maximus.techcore.api.gas.GasCombustionRole;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents oxygen gas (O₂), a common oxidizer in combustion.
 */
public class OxygenGas extends Gas {
    private final float molecularWeight;

    /**
     * Constructs an OxygenGas instance.
     * @param id The unique ID of the gas.
     * @param molecularWeight The molecular weight in g/mol.
     */
    public OxygenGas(ResourceLocation id, float molecularWeight) {
        super(id);
        this.molecularWeight = molecularWeight;
    }

    @Override
    public GasCombustionRole getCombustionRole() {
        return GasCombustionRole.OXYGENATOR;
    }

    @Override
    public float getMolecularWeight() {
        return this.molecularWeight;
    }

    @Override
    public int getColor() {
        return 0x66CCFFFF; // Light blue with full alpha
    }

    @Override
    public float getIgnitionTemperature() {
        return Float.POSITIVE_INFINITY; // Oxygen does not ignite by itself
    }

    @Override
    public float getDefaultPressure() {
        return 1.0f; // 1 atm at STP
    }

    @Override
    public boolean isToxic() {
        return false;
    }
}