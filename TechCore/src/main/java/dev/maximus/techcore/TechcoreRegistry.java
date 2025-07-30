package dev.maximus.techcore;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechcoreRegistry<T> {
    private final Registry<T> registry;
    private final Map<ResourceLocation, Holder.Reference<T>> entries = new HashMap<>();

    public TechcoreRegistry(Registry<T> registry) {
        this.registry = registry;
    }

    public <V extends T> V register(ResourceLocation id, V object) {
        entries.put(id, Registry.registerForHolder(registry, id, object));
        return object;
    }

    public <V extends T> Holder.Reference<V> registerForHolder(ResourceLocation id, V object) {
        var holder = Registry.registerForHolder(registry, id, object);
        entries.put(id, holder);
        return (Holder.Reference<V>) holder;
    }

    public Holder.Reference<T> get(ResourceLocation id) {
        return entries.get(id);
    }

    public List<Holder.Reference<T>> getEntries() {
        return new ArrayList<>(entries.values());
    }
}
