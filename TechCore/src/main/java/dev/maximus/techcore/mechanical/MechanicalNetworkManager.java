package dev.maximus.techcore.mechanical;

import java.util.UUID;

public class MechanicalNetworkManager {
    private final Map<UUID, MechanicalNetwork> networks = new HashMap<>();

    public void tickAll() {
        for (MechanicalNetwork network : networks.values()) {
            network.tick();
        }
    }

    public void addGear(GearBlockEntity gear) { /* Finds/creates network, connects */ }

    public void removeGear(GearBlockEntity gear) { /* Removes from network */ }

    public void addShaft(ShaftBlockEntity shaft) { /* Similar to gear */ }

    public void removeShaft(ShaftBlockEntity shaft) { /* Similar */ }
}