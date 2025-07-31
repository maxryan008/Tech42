package dev.maximus.tech42.mechanical.gears.block_entities;

import dev.maximus.tech42.Constant;
import dev.maximus.techcore.api.mechanical.GearBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WoodGearSmallEntity extends GearBlockEntity {
    public WoodGearSmallEntity(BlockPos pos, BlockState state) {
        super(pos, state, Constant.id("wood_gear_small"));
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, GearBlockEntity blockEntity) {
        this.addYaw(0f);
    }

    @Override
    public <BE extends GearBlockEntity> void init(BE blockEntity) {
        System.out.println("WoodGearSmallEntity init");
    }
}
