package dev.maximus.techcore.mechanical.graph;

import dev.maximus.techcore.api.mechanical.MechanicalNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

public class MechanicalGraphManager {
    private static final MechanicalGraphManager INSTANCE = new MechanicalGraphManager();
    public static MechanicalGraphManager get() { return INSTANCE; }

    private final RoutingGraph routingGraph = new RoutingGraph();
    private final List<GraphCluster> clusters = new ArrayList<>();
    private final Map<BlockPos, MechanicalNode> nodeMap = new HashMap<>();

    public void registerNode(Level level, MechanicalNode node) {
        nodeMap.put(node.pos, node);

        // Assign speedRatio based on any neighbor
        for (BlockPos neighbor : getNeighbors(node.pos)) {
            MechanicalNode other = nodeMap.get(neighbor);
            if (other != null && other.cluster != null) {
                float gearRatio = computeGearRatio(node, other);
                node.speedRatio = other.speedRatio * -gearRatio; // Invert direction

                // Set yaw offset based on neighbor count
                float offset = 360f / (node.config.getToothCount() * 2f);
                node.yawOffset = offset + other.yawOffset;
                node.yaw = offset;
                break; // Only use the first valid neighbor
            }
        }

        // Connect node to RoutingGraph
        routingGraph.registerJunction(node.pos);

        GraphCluster newCluster = new GraphCluster();
        newCluster.addNode(node);

        for (BlockPos neighbor : getNeighbors(node.pos)) {
            MechanicalNode other = nodeMap.get(neighbor);
            if (other == null || other.cluster == null) continue;

            float gearRatio = computeGearRatio(node, other);
            routingGraph.connectJunctions(node.pos, neighbor, gearRatio);

            // Always merge smaller into larger
            if (newCluster.getNodes().size() <= other.cluster.getNodes().size()) {
                mergeClusters(other.cluster, newCluster); // b → a
                return;
            } else {
                mergeClusters(newCluster, other.cluster); // b → a
            }
        }

        clusters.add(newCluster);
    }

    public void unregisterNode(BlockPos pos) {
        MechanicalNode node = nodeMap.remove(pos);
        if (node != null && node.cluster != null) {
            GraphCluster cluster = node.cluster;
            cluster.removeNode(pos);

            // If the cluster is now disconnected, we need to rebuild
            if (cluster.getNodes().isEmpty()) {
                clusters.remove(cluster);
            } else {
                rebuildClusters();
            }

            routingGraph.removeJunction(pos);
        }
    }

    private void rebuildClusters() {
        clusters.clear();
        Set<BlockPos> visited = new HashSet<>();

        for (MechanicalNode node : nodeMap.values()) {
            if (visited.contains(node.pos)) continue;

            GraphCluster cluster = new GraphCluster();
            traverseAndBuildCluster(node, cluster, visited);
            clusters.add(cluster);
        }
    }

    private void traverseAndBuildCluster(MechanicalNode start, GraphCluster cluster, Set<BlockPos> visited) {
        Queue<MechanicalNode> queue = new ArrayDeque<>();
        List<MechanicalNode> ordered = new ArrayList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            MechanicalNode node = queue.poll();
            if (!visited.add(node.pos)) continue;

            ordered.add(node); // Collect for yaw offset assignment

            for (BlockPos neighbor : getNeighbors(node.pos)) {
                MechanicalNode other = nodeMap.get(neighbor);
                if (other != null && validateRPMMatch(node, other)) {
                    queue.add(other);
                }
            }
        }

        for (int i = 0; i < ordered.size(); i++) {
            MechanicalNode node = ordered.get(i);
            float teeth = node.config.getToothCount();
            float offset = 360f / (teeth * 2f) * i;
            node.yawOffset = offset;
            node.yaw = offset;
            cluster.addNode(node);
        }
    }

    public void tick(Level level) {
        for (GraphCluster cluster : clusters) {
            cluster.tick();
        }
    }

    private float computeGearRatio(MechanicalNode a, MechanicalNode b) {
        return a.config.getGearRadius() / b.config.getGearRadius();
    }

    private boolean validateRPMMatch(MechanicalNode a, MechanicalNode b) {
        float ratio = computeGearRatio(a, b);

        // Expected direction-inverted speed ratio
        float expected = a.speedRatio * (-ratio); // Always invert

        // Ensure magnitude and direction match
        return Math.abs(expected - b.speedRatio) < 0.01f;
    }

    private void mergeClusters(GraphCluster a, GraphCluster b) {
        for (MechanicalNode node : b.getNodes()) {
            a.addNode(node);
        }

        clusters.remove(b);

        // No recalculation of speedRatio or yaw — preserve all original values
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        return List.of(
                pos.north(), pos.south(), pos.east(),
                pos.west(), pos.above(), pos.below()
        );
    }

    public MechanicalNode getNode(BlockPos pos) {
        return nodeMap.get(pos);
    }
}