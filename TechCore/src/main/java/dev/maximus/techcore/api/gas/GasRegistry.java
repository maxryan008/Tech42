package dev.maximus.techcore.api.gas;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * A global registry for {@link Gas} instances.
 * <p>
 * Allows mods to register gases using unique {@link ResourceLocation}s,
 * but exposes them for global access by name (ignoring namespace).
 * Only the first gas registered with a given name will be used.
 */
public class GasRegistry {

    /**
     * The full registry mapping ResourceLocation (namespace:path) to Gas.
     * This ensures the uniqueness of gas definitions across mods.
     */
    private static final Map<ResourceLocation, Gas> REGISTRY = new HashMap<>();

    /**
     * A simplified lookup mapping from gas names (e.g. "oxygen") to Gas.
     * Only the first gas registered with a given name is stored here.
     */
    private static final Map<String, Gas> ALIAS_MAP = new HashMap<>();

    /**
     * Registers a new {@link Gas} using the given {@link ResourceLocation}.
     * If another gas has already been registered under the same name (ignoring namespace),
     * that gas will be returned instead and the new gas will not be registered.
     *
     * @param id  The full ID of the gas (e.g., "tech42:oxygen").
     * @param gas The gas instance to register.
     * @return The registered gas, either the provided one if it was accepted,
     *         or the existing gas with the same name if already registered.
     */
    public static Gas register(ResourceLocation id, Gas gas) {
        String key = id.getPath(); // ignore namespace

        if (ALIAS_MAP.containsKey(key)) {
            return ALIAS_MAP.get(key); // already registered
        }

        REGISTRY.put(id, gas);
        ALIAS_MAP.put(key, gas);
        return gas;
    }

    /**
     * Gets a registered gas by name only (ignores namespace).
     * <p>
     * For example, "oxygen" will match any gas registered with a path of "oxygen",
     * regardless of which mod registered it.
     *
     * @param name The name/path of the gas (e.g., "oxygen").
     * @return The registered gas with that name, or {@code null} if not found.
     */
    public static Gas get(String name) {
        return ALIAS_MAP.get(name);
    }

    /**
     * Gets a registered gas by its full {@link ResourceLocation}, including namespace.
     *
     * @param id The full identifier of the gas (e.g., "tech42:oxygen").
     * @return The gas registered under this ID, or {@code null} if not found.
     */
    public static Gas get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    /**
     * @return A map of all registered gases with their full {@link ResourceLocation} IDs.
     */
    public static Map<ResourceLocation, Gas> getAll() {
        return REGISTRY;
    }
}