package dev.maximus.tech42.mechanical.gears.configs;

import dev.maximus.tech42.mechanical.materials.WoodenMechanicalMaterial;
import dev.maximus.techcore.api.mechanical.GearConfig;

public class WoodGearSmall extends GearConfig implements WoodenMechanicalMaterial {
    @Override
    public float getGearRadius() {
        return 0.4f;
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
}
