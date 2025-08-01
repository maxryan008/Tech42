package dev.maximus.techcore.api.mechanical;

import dev.maximus.techcore.mechanical.graph.GraphCluster;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class MechanicalNode {
    public final Level level;
    public final BlockPos pos;
    public final GearConfig config;

    public float inputTorque = 0;
    public float torque = 0;
    public float speed = 0;
    public float speedRatio = 1.0f;
    public float frictionModifier = 1.0f;
    public float brokenTeethRatio = 0f;
    public float yaw = 0;
    public float yawOffset = 0f; // assigned on registration

    public boolean isLubricated = false;
    public int lubricantTicks = 0;

    public GraphCluster cluster = null;

    public MechanicalNode(Level level, BlockPos pos, GearConfig config) {
        this.level = level;
        this.pos = pos;
        this.config = config;
    }

    public void tick() {
        applyFriction();
        decayLubrication();
    }

    private void applyFriction() {
        float friction = config.getFriction() * frictionModifier;
        torque -= friction;
        if (torque < 0) torque = 0;
    }

    private void decayLubrication() {
        if (isLubricated) {
            lubricantTicks--;
            if (lubricantTicks <= 0) {
                isLubricated = false;
                frictionModifier = 1.0f;
            }
        }
    }

    public void applyLubricant(float frictionReduction, int durationTicks) {
        this.frictionModifier = 1.0f - frictionReduction;
        this.lubricantTicks = durationTicks;
        this.isLubricated = true;
    }

    public float getLoad() {
        return (Math.abs(speed) * config.getFriction() + config.getFriction()) * 0.01f;
    }
}