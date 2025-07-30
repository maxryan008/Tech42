package dev.maximus.techcore.api.mechanical;

import java.util.HashSet;
import java.util.Set;

public class MechanicalNetwork {
    private final Set<MechanicalNode> nodes = new HashSet<>();
    private float totalTorque;
    private float averageRpm;

    public void addNode(MechanicalNode node) {
        nodes.add(node);
        node.setNetwork(this);
        recalculate();
    }

    public void removeNode(MechanicalNode node) {
        nodes.remove(node);
        node.setNetwork(null);
        recalculate();
    }

    public void recalculate() {
        // distribute power based on inertia, tooth ratios, stress resistance
    }
}