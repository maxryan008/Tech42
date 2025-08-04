package dev.maximus.techcore.api.mechanical.gear;


import dev.maximus.techcore.api.mechanical.MechanicalNode;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class GearBlock<BE extends GearBlockEntity> extends Block implements EntityBlock {
    private final BiFunction<BlockPos, BlockState, BE> blockEntityFactory;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public GearBlock(BlockBehaviour.Properties settings, BiFunction<BlockPos, BlockState, BE> blockEntityFactory) {
        super(settings);
        this.blockEntityFactory = blockEntityFactory;
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y));
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof GearBlockEntity gear) {
            MechanicalNode node = gear.node;
            if (node != null) {
                if (player.isShiftKeyDown()) {
                    node.inputPower = Math.max(0, node.inputPower - 1);
                    player.displayClientMessage(Component.literal("Power: " + node.inputPower + " W").withStyle(ChatFormatting.RED), true);
                } else {
                    node.inputPower += 1;
                    player.displayClientMessage(Component.literal("Power: " + node.inputPower + " W").withStyle(ChatFormatting.GREEN), true);
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis());
    }
}