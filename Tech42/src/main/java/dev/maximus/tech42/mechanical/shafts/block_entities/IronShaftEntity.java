package dev.maximus.tech42.mechanical.shafts.block_entities;

import dev.maximus.tech42.Constant;
import dev.maximus.tech42.mechanical.shafts.configs.IronShaft;
import dev.maximus.techcore.api.mechanical.shaft.ShaftBlockEntity;
import dev.maximus.techcore.api.mechanical.shaft.ShaftConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IronShaftEntity extends ShaftBlockEntity {
    public IronShaftEntity(BlockPos pos, BlockState state) {
        super(pos, state, Constant.id("iron_shaft"));
    }

    @Override
    public ShaftConfig getConfig() {
        return new IronShaft();
    }
}
