package dev.maximus.techcore.mechanical;

import dev.maximus.techcore.api.mechanical.MechanicalNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class MechanicalNetwork {
    private final Set<MechanicalNode> nodes = new HashSet<>();
    private float networkSpeed = 0f; // Shared speed in RPM
    private float networkYaw = 0f;

    public void build(MechanicalNode start, Set<BlockPos> visited, Map<BlockPos, MechanicalNode> nodeMap) {
        nodes.clear();
        Queue<MechanicalNode> queue = new ArrayDeque<>();
        visited.add(start.pos);
        start.network = this;
        start.speedRatio = 1f;
        nodes.add(start);
        queue.add(start);

        for (MechanicalNode node : nodeMap.values()) {
            node.updateConnections(nodeMap);
        }

        while (!queue.isEmpty()) {
            MechanicalNode current = queue.poll();

            for (MechanicalNode neighbor : current.cachedConnections) {
                if (visited.contains(neighbor.pos)) continue;

                visited.add(neighbor.pos);
                neighbor.network = this;
                nodes.add(neighbor);
                queue.add(neighbor);

                float ratio = current.config.getGearRadius() / neighbor.config.getGearRadius();
                neighbor.speedRatio = -current.speedRatio * ratio;
            }
        }
    }

    public void alignYawsFrom(MechanicalNode seed) {
        Queue<MechanicalNode> queue = new ArrayDeque<>();
        Set<MechanicalNode> visited = new HashSet<>();
        seed.yaw = 0;
        queue.add(seed);
        visited.add(seed);

        while (!queue.isEmpty()) {
            MechanicalNode current = queue.poll();
            for (MechanicalNode neighbor : current.cachedConnections) {
                if (!nodes.contains(neighbor) || visited.contains(neighbor)) continue;

                visited.add(neighbor);
                queue.add(neighbor);

                float neighborToothAngle = 360f / neighbor.config.getToothCount();
                float parentYaw = current.yaw;
                float ratio = neighbor.speedRatio / current.speedRatio;

                float gearYaw = -parentYaw * ratio; // Gear meshing flips direction
                gearYaw = (gearYaw % 360f + 360f) % 360f;
                float snappedYaw = Math.round(gearYaw / neighborToothAngle) * neighborToothAngle + (neighborToothAngle / 2);
                neighbor.yaw = snappedYaw % 360f;
            }
        }
    }

    public boolean canConnect(MechanicalNode a, MechanicalNode b, Direction dir) {
        if (a.isShaft && b.isShaft) return dir.getAxis().isHorizontal() || dir.getAxis().isVertical();
        if (a.isShaft || b.isShaft) return true;

        float expectedDist = a.config.getGearRadius() + b.config.getGearRadius() + a.config.getToothLength() + b.config.getToothLength();
        float actualDist = (float) a.pos.distSqr(b.pos);
        return Math.abs(expectedDist - actualDist) < 0.1f && dir.getAxis().isHorizontal();
    }

    public void tick() {
        float totalInputTorque = 0f;
        float totalInertia = 0f;
        float totalLoad = 0f;

        for (MechanicalNode node : nodes) {
            node.tick();

            float r = node.speedRatio;
            float localInertia = node.config.getInertia() * r * r;
            totalInertia += localInertia;
            totalLoad += node.getLoad();

            if (node.inputTorque > 0.001f) {
                totalInputTorque += node.inputTorque;
            }
        }

        if (totalInertia > 0.0001f) {
            float acceleration = totalInputTorque / totalInertia;
            networkSpeed += acceleration * 0.5f;

            // Apply load as torque directly
            float frictionTorque = totalLoad;
            if (networkSpeed > 0f) {
                networkSpeed -= frictionTorque / totalInertia * 0.5f;
                if (networkSpeed < 0f) networkSpeed = 0f;
            } else if (networkSpeed < 0f) {
                networkSpeed += frictionTorque / totalInertia * 0.5f;
                if (networkSpeed > 0f) networkSpeed = 0f;
            }

            // Damping: natural slowdown
            if (totalInputTorque < 0.001f) {
                networkSpeed *= 0.95f;
                if (Math.abs(networkSpeed) < 0.001f) {
                    networkSpeed = 0;
                }
            }

            // Then update yaw using final damped speed
            networkYaw += (networkSpeed * 6.0f);
            networkYaw %= 360f;
            if (networkYaw < 0f) networkYaw += 360f;

            // Damping: natural slowdown
            if (totalInputTorque < 0.001f) {
                networkSpeed *= 0.95f;
                if (Math.abs(networkSpeed) < 0.001f) {
                    networkSpeed = 0;
                }
            }
        }

        for (MechanicalNode node : nodes) {
            node.speed = node.speedRatio * networkSpeed;
            node.torque = totalInputTorque / Math.max(1, nodes.size());

            if (Math.abs(node.speed) > node.config.getMaxSpeed() || node.torque > node.config.getMaxTorque()) {
                node.level.setBlockAndUpdate(node.pos, Blocks.AIR.defaultBlockState());
            }
        }

        System.out.printf("Torque: %.2f Nm | Load: %.2f | Speed: %.2f RPM | Nodes: %d\n",
                totalInputTorque, totalLoad, networkSpeed, nodes.size());
    }

    public void invalidate() {
        for (MechanicalNode node : nodes) {
            node.network = null;
        }
        nodes.clear();
        networkSpeed = 0f;
    }

    private void failNetwork() {
        for (MechanicalNode node : nodes) {
            node.level.setBlockAndUpdate(node.pos, Blocks.AIR.defaultBlockState());
        }
        nodes.clear();
        networkSpeed = 0f;
    }

    public float getNetworkSpeed() {
        return this.networkSpeed;
    }

    public void setNetworkSpeed(float speed) {
        this.networkSpeed = speed;
    }

    public Set<MechanicalNode> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public float getNetworkYaw() {
        return this.networkYaw;
    }

    public void setNetworkYaw(float yaw) {
        this.networkYaw = yaw;
    }

    public float getNetTorque() {
        float torque = 0f;
        for (MechanicalNode node : nodes) {
            float ratio = node.speedRatio;
            torque += node.inputTorque + (node.config.getInertia() * ratio * ratio);
        }
        return torque;
    }
}