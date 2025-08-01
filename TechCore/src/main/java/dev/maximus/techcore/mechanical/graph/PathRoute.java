package dev.maximus.techcore.mechanical.graph;

import java.util.Set;

public record PathRoute(Set<Integer> nodeSet, float gearRatio) {
    public boolean equalsByNodes(PathRoute other) {
        return this.nodeSet.equals(other.nodeSet);
    }
}