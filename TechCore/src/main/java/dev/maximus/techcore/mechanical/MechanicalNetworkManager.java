package dev.maximus.techcore.mechanical;

import dev.maximus.techcore.api.mechanical.MechanicalNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.*;

public class MechanicalNetworkManager {
    private final Set<MechanicalNetwork> networks = new HashSet<>();
    private final Map<BlockPos, MechanicalNode> nodeMap = new HashMap<>();

    private static final MechanicalNetworkManager INSTANCE = new MechanicalNetworkManager();

    private boolean needsRebuild = false;

    public static MechanicalNetworkManager get(Level level) {
        return INSTANCE; // For now, singleton
    }

    public void register(MechanicalNode node) {
        nodeMap.put(node.pos, node);
        needsRebuild = true;
    }

    public void unregister(MechanicalNode node) {
        nodeMap.remove(node.pos);
        needsRebuild = true;
    }

    public void tick(ServerLevel level) {
        if (needsRebuild) {
            rebuildNetworks(false);
            needsRebuild = false;
        }

        for (MechanicalNetwork net : networks) {
            net.tick();
        }
    }

    public void markDirty() {
        needsRebuild = true;
    }

    private void rebuildNetworks(boolean realignYaws) {
        Map<Set<BlockPos>, Float> oldNetworkSpeeds = new HashMap<>();
        Map<Set<BlockPos>, Float> oldNetworkYaws = new HashMap<>();
        Map<Set<BlockPos>, MechanicalNode> networkSeeds = new HashMap<>();

        for (MechanicalNetwork net : networks) {
            Set<BlockPos> positions = new HashSet<>();
            MechanicalNode seed = null;
            for (MechanicalNode node : net.getNodes()) {
                positions.add(node.pos.immutable());
                if (seed == null) seed = node;
            }
            oldNetworkSpeeds.put(positions, net.getNetworkSpeed());
            oldNetworkYaws.put(positions, net.getNetworkYaw());
            networkSeeds.put(positions, seed);
        }

        networks.clear();
        Set<BlockPos> visited = new HashSet<>();

        for (MechanicalNode node : nodeMap.values()) {
            if (!visited.contains(node.pos)) {
                MechanicalNetwork newNet = new MechanicalNetwork();
                newNet.build(node, visited, nodeMap);

                float inheritedSpeed = 0f;
                float inheritedYaw = 0f;
                MechanicalNode alignmentSeed = node;
                int maxOverlap = 0;

                for (Map.Entry<Set<BlockPos>, Float> entry : oldNetworkSpeeds.entrySet()) {
                    Set<BlockPos> oldPos = entry.getKey();
                    int overlap = 0;
                    for (MechanicalNode n : newNet.getNodes()) {
                        if (oldPos.contains(n.pos.immutable())) overlap++;
                    }

                    if (overlap > maxOverlap) {
                        maxOverlap = overlap;
                        inheritedSpeed = entry.getValue();
                        inheritedYaw = oldNetworkYaws.get(entry.getKey());
                        alignmentSeed = networkSeeds.get(entry.getKey());
                    }
                }

                newNet.setNetworkSpeed(inheritedSpeed);
                newNet.setNetworkYaw(inheritedYaw);

                if (realignYaws) {
                    newNet.alignYawsFrom(alignmentSeed);
                }
                networks.add(newNet);
            }
        }
    }

    public MechanicalNode getNode(BlockPos pos) {
        return nodeMap.get(pos);
    }

    public void tickNode(MechanicalNode node) {
        if (node.network == null) {
            rebuildNetworks(false);
        }
    }

    public void addAndAlignNode(MechanicalNode node) {
        Map<MechanicalNetwork, Set<MechanicalNode>> neighborNetworks = new HashMap<>();
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = node.pos.relative(dir);
            MechanicalNode neighbor = nodeMap.get(neighborPos);
            if (neighbor != null && neighbor.network != null) {
                neighborNetworks.computeIfAbsent(neighbor.network, k -> new HashSet<>()).add(neighbor);
            }
        }

        register(node); // adds node to nodeMap

        if (neighborNetworks.isEmpty()) {
            rebuildNetworks(false); // no alignment needed
            return;
        }

        if (neighborNetworks.size() == 1) {
            // Case 1: One neighboring network → align only the new node
            MechanicalNetwork network = neighborNetworks.keySet().iterator().next();
            MechanicalNode seed = neighborNetworks.get(network).iterator().next();

            node.network = network;
            node.updateConnections(nodeMap);
            float ratio = seed.speedRatio * (seed.config.getGearRadius() / node.config.getGearRadius());
            node.speedRatio = -ratio;

            float anglePerTooth = 360f / node.config.getToothCount();
            float expectedYaw = -seed.yaw * (node.speedRatio / seed.speedRatio);
            expectedYaw = (expectedYaw % 360f + 360f) % 360f;
            node.yaw = Math.round(expectedYaw / anglePerTooth) * anglePerTooth + (anglePerTooth / 2f);
        } else {
            // Case 2: Multiple networks → merge and align
            MechanicalNetwork strongest = Collections.max(neighborNetworks.keySet(),
                    Comparator.comparingDouble(MechanicalNetwork::getNetTorque));

            // force rebuild, and align using seed from strongest network
            rebuildNetworks(true); // will align using seed
        }
    }
}