package dev.maximus.tech42.mechanical.shafts.configs;

import dev.maximus.techcore.api.mechanical.shaft.ShaftConfig;

public class IronShaft extends ShaftConfig {
    @Override
    public float getShaftRadius() {
        return 0.2f;
    }

    @Override
    public float getMass() {
        return 10;
    }

    @Override
    public float getMaxTorque() {
        return 100;
    }

    @Override
    public boolean supportsLubrication() {
        return false;
    }

    @Override
    public float getLubricationDecayRate() {
        return 0;
    }
}
