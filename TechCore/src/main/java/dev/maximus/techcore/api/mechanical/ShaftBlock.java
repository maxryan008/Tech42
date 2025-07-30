package dev.maximus.techcore.api.mechanical;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ShaftBlock<BE extends BlockEntity> extends Block implements EntityBlock {
    private final BiFunction<BlockPos, BlockState, BE> blockEntityFactory;

    public ShaftBlock(BlockBehaviour.Properties settings, BiFunction<BlockPos, BlockState, BE> blockEntityFactory) {
        super(settings);
        this.blockEntityFactory = blockEntityFactory;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFactory.apply(pos, state);
    }
}