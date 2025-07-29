package dev.maximus.techcore.api.substance;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record SubstanceContext(Level level, BlockPos pos) {}