package dev.maximus.techcore.api.mechanical;

import dev.maximus.techcore.TechcoreBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ShaftBlockEntity extends BlockEntity {
    protected final ResourceLocation id;

    public ShaftBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        super(TechcoreBlockEntities.BLOCK_ENTITIES.get(id).value(), pos, state);
        this.id = id;
    }

    public abstract void serverTick(Level level, BlockPos pos, BlockState state, ShaftBlockEntity blockEntity);

    public abstract <BE extends ShaftBlockEntity> void init(BE blockEntity);

    public ResourceLocation getMultiblockId() {
        return this.id;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (!level.isClientSide()) {
            this.init(this);
        }
    }
}
