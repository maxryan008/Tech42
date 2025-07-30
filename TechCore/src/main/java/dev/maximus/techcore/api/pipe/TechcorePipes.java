package dev.maximus.techcore.api.pipe;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class TechcorePipes {
    private static final Map<Block, PipeType> PIPE_TYPES = new HashMap<>();

    public static void registerPipe(Block block, PipeType type) {
        PIPE_TYPES.put(block, type);
    }

    public static PipeType getPipe(Block block) {
        return PIPE_TYPES.get(block);
    }

    public static Map<Block, PipeType> getAll() {
        return PIPE_TYPES;
    }
}