package dev.maximus.techcore.api.mechanical.shaft;

import dev.maximus.techcore.api.mechanical.gear.GearMaterial;
import net.minecraft.resources.ResourceLocation;

public class ShaftType {
    private final ResourceLocation id;
    private final GearMaterial material;
    private final float maxRpm;
    private final float maxTorque;
    private final float maxStress;

    public ShaftType(ResourceLocation id, GearMaterial material, float maxRpm, float maxTorque, float maxStress) {
        this.id = id;
        this.material = material;
        this.maxRpm = maxRpm;
        this.maxTorque = maxTorque;
        this.maxStress = maxStress;
    }

    public ResourceLocation getId() { return id; }
    public GearMaterial getMaterial() { return material; }
    public float getMaxRpm() { return maxRpm; }
    public float getMaxTorque() { return maxTorque; }
    public float getMaxStress() { return maxStress; }
}