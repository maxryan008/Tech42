package dev.maximus.techcore.api.substance;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a chemical substance that can exist in multiple physical states (solid, liquid, gas).
 */
public abstract class Substance {
    private final ResourceLocation id;

    protected Substance(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public abstract float getMolecularWeight(); // g/mol

    public abstract int getColor(); // ARGB

    public abstract boolean isToxic();

    /**
     * Returns the phase state of this substance under the given conditions.
     * @param temperatureC Temperature in Celsius.
     * @param pressureAtm Pressure in atmospheres.
     */
    public PhaseState getPhase(float temperatureC, float pressureAtm) {
        float tempK = temperatureC + 273.15f;
        float pressureRef = 1.0f;

        float boilingPoint = getBoilingPoint();
        float meltingPoint = getMeltingPoint();

        float deltaHvap = getEnthalpyVaporization();
        float deltaHfus = getEnthalpyFusion();

        float R = 8.314f;

        float boilAtP = (1 / ((1 / boilingPoint) - (R / deltaHvap) * (float)Math.log(pressureAtm / pressureRef)));
        float meltAtP = meltingPoint + 0.01f * (pressureAtm - pressureRef); // Simplified linear shift

        if (tempK < meltAtP) return PhaseState.SOLID;
        if (tempK < boilAtP) return PhaseState.LIQUID;
        return PhaseState.GAS;
    }

    /**
     * Melting point in Kelvin (solid to liquid at 1 atm).
     */
    public abstract float getMeltingPoint();

    /**
     * Boiling point in Kelvin (liquid to gas at 1 atm).
     */
    public abstract float getBoilingPoint();

    /**
     * Default pressure in atm (e.g., 1.0 atm).
     */
    public float getDefaultPressure() {
        return 1.0f;
    }

    /**
     * Default temperature in Kelvin (298.15K = 25°C).
     */
    public float getDefaultTemperature() {
        return 298.15f;
    }

    public abstract CombustionRole getCombustionRole();

    public abstract float getEnthalpyVaporization(); // ΔHvap (J/mol)
    public abstract float getEnthalpyFusion(); // ΔHfus (J/mol)

    public void onTick(SubstanceStack stack, SubstanceContext context) {}
}