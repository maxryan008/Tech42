package dev.maximus.techcore.api.mechanical.shaft;

import dev.maximus.techcore.api.mechanical.MechanicalConfig;

public abstract class ShaftConfig extends MechanicalConfig {
    public abstract float getShaftRadius();

    @Override
    public float getInertia() {
        float r = getShaftRadius(); // radius in meters
        float m = getMass();       // mass in kg
        return 0.5f * m * r * r;
    }
}