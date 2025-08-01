package dev.maximus.techcore.mechanical.graph;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class JunctionNode {
    public final int id;
    public final BlockPos pos;
    private final Map<Integer, PathRoute> routes = new HashMap<>();

    public JunctionNode(int id, BlockPos pos) {
        this.id = id;
        this.pos = pos;
    }

    public void addRoute(int targetId, PathRoute route) {
        PathRoute existing = routes.get(targetId);
        if (existing == null || !existing.nodeSet().equals(route.nodeSet())) {
            routes.put(targetId, route);
        }
    }

    public void removeRoute(int targetId) {
        routes.remove(targetId);
    }

    public boolean hasRouteTo(int targetId) {
        return routes.containsKey(targetId);
    }

    public PathRoute getRouteTo(int targetId) {
        return routes.get(targetId);
    }

    public Map<Integer, PathRoute> getAllRoutes() {
        return routes;
    }
}