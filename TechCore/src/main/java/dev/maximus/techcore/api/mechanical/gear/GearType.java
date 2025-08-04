package dev.maximus.techcore.api.mechanical.gear;

import net.minecraft.resources.ResourceLocation;

public class GearType {
    private final ResourceLocation id;
    private final int toothCount;
    private final GearMaterial material;
    private final int size; // in blocks: 1 = 1x1, 2 = 2x2, 3 = 3x3
    private final float stressPerRadian; // stress generated per radian rotation
    private final float pitchDiameter;   // optional: in blocks

    public GearType(ResourceLocation id, int toothCount, int size, float pitchDiameter, float stressPerRadian, GearMaterial material) {
        this.id = id;
        this.toothCount = toothCount;
        this.size = size;
        this.pitchDiameter = pitchDiameter;
        this.stressPerRadian = stressPerRadian;
        this.material = material;
    }

    public float getSpeedRatio(GearType other) {
        return (float) other.toothCount / this.toothCount;
    }

    public float getTorqueRatio(GearType other) {
        return (float) this.toothCount / other.toothCount;
    }

    public float getInertia() {
        return material.baseInertia() * size * size * material.density();
    }

    // Getters
    public ResourceLocation getId() { return id; }
    public int getToothCount() { return toothCount; }
    public int getSize() { return size; }
    public float getStressPerRadian() { return stressPerRadian; }
    public float getPitchDiameter() { return pitchDiameter; }
    public GearMaterial getMaterial() { return material; }
}