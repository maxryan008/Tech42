package dev.maximus.techcore.api.mechanical;

public abstract class GearConfig {
    public abstract float getGearRadius();
    public abstract int getToothCount();
    public abstract float getGearWidth();
    public abstract float getToothLength(); // how far teeth extend radially beyond the core
    public abstract float getToothWidth();  // width of each tooth
    public abstract float getToothHeight(); // height of the tooth (Y axis)
}