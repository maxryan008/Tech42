package dev.maximus.techcore.api.mechanical;


import dev.maximus.techcore.mechanical.MechanicalNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class GearBlock<BE extends GearBlockEntity> extends Block implements EntityBlock {
    private final BiFunction<BlockPos, BlockState, BE> blockEntityFactory;

    public GearBlock(BlockBehaviour.Properties settings, BiFunction<BlockPos, BlockState, BE> blockEntityFactory) {
        super(settings);
        this.blockEntityFactory = blockEntityFactory;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFactory.apply(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return GearBlockEntity::tick;
    }

    //FIXME remove later

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof GearBlockEntity gear) {
            MechanicalNode node = gear.node;
            if (node != null) {
                if (player.isShiftKeyDown()) {
                    node.inputTorque = Math.max(0, node.inputTorque - 1);
                    player.displayClientMessage(Component.literal("Torque: " + node.inputTorque + " Nm").withStyle(ChatFormatting.RED), true);
                } else {
                    node.inputTorque += 1;
                    player.displayClientMessage(Component.literal("Torque: " + node.inputTorque + " Nm").withStyle(ChatFormatting.GREEN), true);
                }
            }
        }

        return InteractionResult.CONSUME;
    }
}