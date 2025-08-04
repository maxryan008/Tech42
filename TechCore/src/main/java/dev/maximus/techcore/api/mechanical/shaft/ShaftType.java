package dev.maximus.techcore.api.mechanical.shaft;

import dev.maximus.techcore.api.mechanical.gear.GearMaterial;
import net.minecraft.resources.ResourceLocation;

public class ShaftType {
    private final ResourceLocation id;
    private final ShaftMaterial material;
    private final int size; // e.g., 1 = 1m long, or 1x1 shaft

    public ShaftType(ResourceLocation id, ShaftMaterial material, int size) {
        this.id = id;
        this.material = material;
        this.size = size;
    }

    public ResourceLocation getId() { return id; }
    public ShaftMaterial getMaterial() { return material; }
    public int getSize() { return size; }

    public float getMaxTorque() { return material.maxTorque(); }
    public float getInertia() { return material.baseInertia() * size * material.density(); }
}