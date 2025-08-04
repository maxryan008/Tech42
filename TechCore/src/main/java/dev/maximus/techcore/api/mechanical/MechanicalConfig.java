package dev.maximus.techcore.api.mechanical;

public abstract class MechanicalConfig {
    public abstract float getMass();          // kg
    public abstract float getMaxTorque();     // Nm
    public abstract float getInertia();

    // Lubrication behavior
    public abstract boolean supportsLubrication();
    public abstract float getLubricationDecayRate(); // % per tick (0.01 = 1%)
}
