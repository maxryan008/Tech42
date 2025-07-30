package dev.maximus.techcore.api.pipe;

import net.minecraft.core.BlockPos;

public record PipeContext(
        PipeNetwork network,
        BlockPos pos,
        PipeState state
) {}