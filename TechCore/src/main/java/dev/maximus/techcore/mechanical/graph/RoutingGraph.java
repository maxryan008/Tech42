package dev.maximus.techcore.mechanical.graph;

import net.minecraft.core.BlockPos;

import java.util.*;

public class RoutingGraph {
    private final Map<BlockPos, Integer> splitIdMap = new HashMap<>();
    private final Map<Integer, BlockPos> idToSplitMap = new HashMap<>();
    private final Map<Integer, JunctionNode> junctions = new HashMap<>();
    private final Map<Integer, Set<Integer>> adjacency = new HashMap<>();
    private int nextId = 0;

    public int registerJunction(BlockPos pos) {
        return splitIdMap.computeIfAbsent(pos, p -> {
            int id = nextId++;
            idToSplitMap.put(id, pos);
            junctions.put(id, new JunctionNode(id, p));
            return id;
        });
    }

    public void connectJunctions(BlockPos from, BlockPos to, float gearRatio) {
        int fromId = registerJunction(from);
        int toId = registerJunction(to);

        adjacency.computeIfAbsent(fromId, k -> new HashSet<>()).add(toId);
        adjacency.computeIfAbsent(toId, k -> new HashSet<>()).add(fromId);

        Set<PathRoute> newRoutes = buildUniquePaths(fromId, toId, gearRatio);
        for (PathRoute route : newRoutes) {
            junctions.get(fromId).addRoute(toId, route);
            junctions.get(toId).addRoute(fromId, new PathRoute(route.nodeSet(), 1f / route.gearRatio()));
        }
    }

    private Set<PathRoute> buildUniquePaths(int fromId, int toId, float gearRatio) {
        Set<PathRoute> result = new HashSet<>();
        Set<Integer> visited = new HashSet<>();
        LinkedList<Integer> path = new LinkedList<>();
        dfs(fromId, toId, gearRatio, visited, path, result);
        return result;
    }

    private void dfs(int current, int target, float gearRatio,
                     Set<Integer> visited, LinkedList<Integer> path,
                     Set<PathRoute> routes) {
        if (!visited.add(current)) return;
        path.add(current);

        if (current == target) {
            routes.add(new PathRoute(Set.copyOf(path), gearRatio));
        } else {
            for (int neighbor : adjacency.getOrDefault(current, Set.of())) {
                dfs(neighbor, target, gearRatio, visited, path, routes);
            }
        }

        path.removeLast();
        visited.remove(current);
    }

    public void removeJunction(BlockPos pos) {
        Integer id = splitIdMap.remove(pos);
        if (id != null) {
            idToSplitMap.remove(id);
            junctions.remove(id);
            adjacency.remove(id);
            for (Set<Integer> neighbors : adjacency.values()) {
                neighbors.remove(id);
            }
            for (JunctionNode j : junctions.values()) {
                j.removeRoute(id);
            }
        }
    }

    public boolean areConnected(int fromId, int toId) {
        return junctions.containsKey(fromId) && junctions.get(fromId).hasRouteTo(toId);
    }

    public JunctionNode getJunction(BlockPos pos) {
        Integer id = splitIdMap.get(pos);
        return id != null ? junctions.get(id) : null;
    }

    public int getId(BlockPos pos) {
        return splitIdMap.getOrDefault(pos, -1);
    }

    public BlockPos getPos(int id) {
        return idToSplitMap.get(id);
    }
}