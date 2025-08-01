package dev.maximus.techcore.mechanical.graph;

import dev.maximus.techcore.api.mechanical.MechanicalNode;
import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GraphCluster {
    private final Map<BlockPos, MechanicalNode> nodes = new HashMap<>();
    private float networkRPM = 0f;
    private float networkYaw = 0f;

    public void addNode(MechanicalNode node) {
        nodes.put(node.pos, node);
        node.cluster = this;
    }

    public void removeNode(BlockPos pos) {
        MechanicalNode node = nodes.remove(pos);
        if (node != null) node.cluster = null;
    }

    public boolean contains(BlockPos pos) {
        return nodes.containsKey(pos);
    }

    public Collection<MechanicalNode> getNodes() {
        return nodes.values();
    }

    public void tick() {
        float inputTorque = 0f;
        float totalInertia = 0f;
        float totalLoad = 0f;

        for (MechanicalNode node : nodes.values()) {
            node.tick();
            float ratio = node.speedRatio;
            totalInertia += node.config.getInertia() * ratio * ratio;
            totalLoad += node.getLoad();

            if (node.inputTorque > 0.001f) {
                inputTorque += node.inputTorque;
            }
        }

        if (totalInertia > 0.0001f) {
            float accel = inputTorque / totalInertia;
            networkRPM += accel * 0.5f;

            float friction = totalLoad;
            if (networkRPM > 0f) {
                networkRPM -= friction / totalInertia * 0.5f;
                if (networkRPM < 0f) networkRPM = 0f;
            } else if (networkRPM < 0f) {
                networkRPM += friction / totalInertia * 0.5f;
                if (networkRPM > 0f) networkRPM = 0f;
            }

            // Damping
            if (inputTorque < 0.001f) {
                networkRPM *= 0.95f;
                if (Math.abs(networkRPM) < 0.001f) networkRPM = 0f;
            }
        }

        networkYaw += (networkRPM * 6.0f);
        networkYaw %= 360f;

        for (MechanicalNode node : nodes.values()) {
            node.speed = node.speedRatio * networkRPM;
            node.torque = inputTorque / Math.max(1, nodes.size());
            node.yaw = (networkYaw * node.speedRatio) % 360f;
        }
    }

    public float getNetworkRPM() {
        return networkRPM;
    }

    public float getYaw() {
        return networkYaw;
    }
}