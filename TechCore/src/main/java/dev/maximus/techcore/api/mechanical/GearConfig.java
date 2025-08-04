package dev.maximus.techcore.api.mechanical;

/**
 * Configuration for a gear type. These values are static for each gear type and do not change at runtime.
 * Dynamic state (like torque, speed, or lubricant) should be stored in MechanicalNode.
 */
public abstract class GearConfig {

    // Visual / structural properties
    public abstract float getGearRadius();      // in blocks (0.5f = half block)
    public abstract int getToothCount();
    public abstract float getGearWidth();       // Z or X dimension of gear thickness
    public abstract float getToothLength();     // How far teeth extend out radially
    public abstract float getToothWidth();
    public abstract float getToothHeight();     // Y height of the tooth

    // Physical properties
    public abstract float getMass();            // kg (affects inertia)
    public abstract float getStaticFriction(); // Nm
    public abstract float getDynamicFriction();  // Nm·s/rad
    public abstract float getMaxTorque();       // Nm (above this: tooth damage)
    public abstract float getMaxSpeed();        // rad/tick or degrees/tick (above this: shatter)
    public float getInertia() {
        float r = getGearRadius(); // radius in meters
        float m = getMass();       // mass in kg
        return 0.5f * m * r * r;
    }

    // Lubrication behavior
    public abstract boolean supportsLubrication();
    public abstract float getLubricationDecayRate(); // % per tick (0.01 = 1%)
}