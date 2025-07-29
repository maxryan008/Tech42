package dev.maximus.techcore.api.gas;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a unique gas type with physical and chemical properties.
 */
public abstract class Gas {
    private final ResourceLocation id;

    /**
     * Constructs a gas with a unique ID.
     * @param id The unique ResourceLocation ID for the gas.
     */
    protected Gas(ResourceLocation id) {
        this.id = id;
    }

    /**
     * @return The ResourceLocation ID of this gas.
     */
    public ResourceLocation getId() {
        return id;
    }

    /**
     * @return The role this gas plays in combustion reactions.
     */
    public abstract GasCombustionRole getCombustionRole();

    /**
     * @return The molecular weight of the gas in g/mol.
     */
    public abstract float getMolecularWeight();

    /**
     * @return The ARGB color used for rendering the gas.
     */
    public abstract int getColor();

    /**
     * @return The temperature in Celsius at which this gas reacts in a combustible mix.
     */
    public abstract float getIgnitionTemperature();

    /**
     * @return The default pressure of this gas in atm.
     */
    public abstract float getDefaultPressure();

    /**
     * @return Whether this gas is harmful when inhaled.
     */
    public abstract boolean isToxic();

    /**
     * Called every tick to allow custom world interactions.
     * @param stack The gas stack.
     * @param context The context/environment for this gas.
     */
    public void onTick(GasStack stack, GasContext context) {}
}