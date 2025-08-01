package dev.maximus.tech42.mechanical.gears.configs;

import dev.maximus.tech42.mechanical.materials.WoodenMechanicalMaterial;
import dev.maximus.techcore.api.mechanical.GearConfig;

public class WoodGearSmall extends GearConfig implements WoodenMechanicalMaterial {
    @Override
    public float getGearRadius() {
        return 0.4f; // 0.8 block diameter
    }

    @Override
    public int getToothCount() {
        return 8;
    }

    @Override
    public float getGearWidth() {
        return 0.2f;
    }

    @Override
    public float getToothLength() {
        return 0.125f;
    }

    @Override
    public float getToothWidth() {
        return 0.09375f;
    }

    @Override
    public float getToothHeight() {
        return 0.25f;
    }

    // --- Physical properties ---

    @Override
    public float getMass() {
        return 10f; // kilograms
    }

    @Override
    public float getFriction() {
        return 0.15f; // relatively high for wood
    }

    @Override
    public float getMaxTorque() {
        return 6.0f; // Nm; lightweight gear
    }

    @Override
    public float getMaxSpeed() {
        return 300.0f; // safe max RPM for small wood gear
    }

    // --- Lubrication behavior ---

    @Override
    public boolean supportsLubrication() {
        return true;
    }

    @Override
    public float getLubricationDecayRate() {
        return 0.03f; // loses 3% effectiveness per tick
    }
}