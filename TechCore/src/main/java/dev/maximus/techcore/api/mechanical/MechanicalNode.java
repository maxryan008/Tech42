package dev.maximus.techcore.api.mechanical;

import dev.maximus.techcore.mechanical.MechanicalNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MechanicalNode {
    public final Level level;
    public final BlockPos pos;
    public final GearConfig config;

    public float inputTorque = 0;
    public float torque = 0;
    public float speed = 0;
    public float frictionModifier = 1.0f;
    public float brokenTeethRatio = 0f;
    public float yaw = 0;
    public float speedRatio = 1.0f;

    public boolean isLubricated = false;
    public int lubricantTicks = 0;

    public MechanicalNetwork network = null;

    public boolean isShaft;

    public Direction facing;
    public Set<MechanicalNode> cachedConnections = new HashSet<>();

    public MechanicalNode(Level level, BlockPos pos, GearConfig config, boolean isShaft) {
        this.level = level;
        this.pos = pos;
        this.config = config;
        this.isShaft = isShaft;
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

    public void updateConnections(Map<BlockPos, MechanicalNode> nodeMap) {
        cachedConnections.clear();
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            MechanicalNode other = nodeMap.get(neighbor);
            if (other != null && network != null && network.canConnect(this, other, dir)) {
                cachedConnections.add(other);
            }
        }
    }

    public float getLoad() {
        float load = (Math.abs(speed) * config.getFriction() + config.getFriction()) * 0.01f;

        return load;
    }
}