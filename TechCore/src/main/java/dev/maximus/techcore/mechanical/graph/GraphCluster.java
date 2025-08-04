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
        float totalPowerInput = 0f;
        float totalLoadTorque = 0f;
        float totalInertia = 0f;

        // Step 1: Tick all nodes, sum up power and torque demand
        for (MechanicalNode node : nodes.values()) {
            node.tick();

            totalInertia += node.config.getInertia() * node.speedRatio * node.speedRatio;

            if (node.inputPower > 0.001f) {
                totalPowerInput += node.inputPower;
            }

            float localRPM = networkRPM * Math.abs(node.speedRatio);
            totalLoadTorque += node.getTotalFrictionTorque(localRPM);
        }

        // Step 2: Estimate equilibrium RPM based on power and load
        float targetRPM = 0f;
        if (totalLoadTorque > 0.001f && totalPowerInput > 0.001f) {
            float omegaEquilibrium = totalPowerInput / totalLoadTorque; // rad/s
            targetRPM = (float) (omegaEquilibrium * 60.0 / (2 * Math.PI)); // convert to RPM
        }

        // Step 3: Gradually accelerate toward targetRPM based on inertia
        if (totalInertia > 0.0001f) {
            float deltaRPM = targetRPM - networkRPM;
            float accel = deltaRPM / totalInertia;
            networkRPM += accel * 0.5f;
        }

        // Damping if no power is applied
        if (totalPowerInput < 0.001f && totalLoadTorque > 0.001f && totalInertia > 0.0001f) {
            // Apply negative acceleration due to friction torque
            float angularDecel = totalLoadTorque / totalInertia; // RPM per tick
            float delta = angularDecel * 0.5f;

            System.out.println();

            // Decelerate toward 0
            if (networkRPM > delta) {
                networkRPM -= delta;
            } else if (networkRPM < -delta) {
                networkRPM += delta;
            } else {
                networkRPM = 0f;
            }
        }

        networkYaw += networkRPM * 0.1f;
        networkYaw %= 360f;

        for (MechanicalNode node : nodes.values()) {
            node.speed = networkRPM * Math.abs(node.speedRatio);

            // Avoid div by zero; if speed is ~0, assume max torque output at stall
            float omega = (float) (Math.abs(node.speed) * 2 * Math.PI / 60f);
            node.torque = (Math.abs(omega) > 0.001f)
                    ? node.inputPower / omega
                    : node.inputPower / 0.001f;

            node.yaw = (networkYaw * node.speedRatio) % 360f;
        }

        float totalPower = 0f;
        float totalSpeed = 0f;
        float totalTorque = 0f;

        for (MechanicalNode node : nodes.values()) {
            totalPower += node.inputPower;
            totalSpeed += Math.abs(node.speed);
            totalTorque += node.torque;
        }

        int count = nodes.size();
        float avgSpeed = count > 0 ? totalSpeed / count : 0f;
        float avgTorque = count > 0 ? totalTorque / count : 0f;
        float avgLoad = count > 0 ? totalLoadTorque / count : 0f;

        System.out.printf("[Cluster] Power: %.2f W | Avg Speed: %.2f RPM | Avg Torque: %.2f Nm | Load: %.2f Nm | Nodes: %d%n", totalPower, avgSpeed, avgTorque, avgLoad, count);
    }

    public float getNetworkRPM() {
        return networkRPM;
    }

    public float getYaw() {
        return networkYaw;
    }
}