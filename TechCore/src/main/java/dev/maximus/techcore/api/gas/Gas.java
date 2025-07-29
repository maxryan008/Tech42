package dev.maximus.techcore.api.gas;

import net.minecraft.resources.ResourceLocation;

public abstract class Gas {
    private final ResourceLocation id;

    protected Gas(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    public abstract boolean isCombustible();
    public abstract boolean isToxic();
    public abstract float getMolecularWeight();
    public abstract int getColor(); // ARGB for rendering
    public abstract float getIgnitionTemperature();
    public abstract float getDefaultPressure();

    public void onTick(GasStack stack, GasContext context) {
        // Optional world effects: poisoning, combustion, dispersion
    }
}