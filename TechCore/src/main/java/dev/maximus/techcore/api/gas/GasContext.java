package dev.maximus.techcore.api.gas;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class GasContext {
    public final Level world;
    public final BlockPos pos;
    public final Direction direction;
    public final GasStack stack;

    public GasContext(Level world, BlockPos pos, Direction direction, GasStack stack) {
        this.world = world;
        this.pos = pos;
        this.direction = direction;
        this.stack = stack;
    }

    public void ignite() {
        // spawn flame particles, damage, sound, etc.
    }

    public void poisonNearbyEntities() {
        // Apply status effects in radius
    }
}
