package dev.maximus.techcore.api.substance;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SubstanceRegistry {
    private static final Map<ResourceLocation, Substance> SUBSTANCES = new HashMap<>();

    public static void register(ResourceLocation id, Substance substance) {
        SUBSTANCES.put(id, substance);
    }

    public static Substance get(ResourceLocation id) {
        return SUBSTANCES.get(id);
    }

    public static Iterable<Substance> getAll() {
        return SUBSTANCES.values();
    }
}