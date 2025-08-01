package dev.maximus.tech42.mechanical.gears.configs;

import dev.maximus.tech42.mechanical.materials.WoodenMechanicalMaterial;
import dev.maximus.techcore.api.mechanical.GearConfig;

public class IronGearSmall extends GearConfig implements WoodenMechanicalMaterial {
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
        return 100f; // kilograms
    }

    @Override
    public float getFriction() {
        return 10f;
    }

    @Override
    public float getMaxTorque() {
        return 500; // Nm
    }

    @Override
    public float getMaxSpeed() {
        return 16000.0f; //rpm
    }

    // --- Lubrication behavior ---

    @Override
    public boolean supportsLubrication() {
        return true;
    }

    @Override
    public float getLubricationDecayRate() {
        return 0.01f;
    }
}