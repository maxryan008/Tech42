package dev.maximus.techcore.api.mechanical;

public record GearMaterial(
        String name,
        float maxRpmWithoutLubrication,
        float maxStress,
        boolean requiresLubricationAboveThreshold,
        float lubricationStressFactor, // e.g., 0.5f = 50% stress when lubricated
        float brittleness,             // 0.0 (ductile) to 1.0 (brittle) — breaks easily under shock
        float density,                 // kg/m^3 — used for inertia
        float baseInertia              // base inertia multiplier for 1x1 gear
) {}