package dev.maximus.techcore.api.mechanical.shaft;

public record ShaftMaterial(
        String name,
        float maxTorque,     // Nm
        float density,       // kg/m³
        float baseInertia    // multiplier
) {}