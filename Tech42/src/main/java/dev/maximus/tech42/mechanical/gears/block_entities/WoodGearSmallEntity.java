package dev.maximus.tech42.mechanical.gears.block_entities;

import dev.maximus.tech42.Constant;
import dev.maximus.tech42.mechanical.gears.configs.WoodGearSmall;
import dev.maximus.techcore.api.mechanical.GearBlockEntity;
import dev.maximus.techcore.api.mechanical.GearConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WoodGearSmallEntity extends GearBlockEntity {
    public WoodGearSmallEntity(BlockPos pos, BlockState state) {
        super(pos, state, Constant.id("wood_gear_small"));
    }

    @Override
    public GearConfig getConfig() {
        return new WoodGearSmall();
    }
}
