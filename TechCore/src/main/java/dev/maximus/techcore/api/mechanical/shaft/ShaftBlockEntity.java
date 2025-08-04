package dev.maximus.techcore.api.mechanical.shaft;

import dev.maximus.techcore.TechcoreBlockEntities;
import dev.maximus.techcore.api.mechanical.MechanicalNode;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import dev.maximus.techcore.mechanical.graph.MechanicalGraphManager;
import dev.maximus.techcore.model.ModelBuilder;
import dev.maximus.techcore.model.QuadGeometryData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public abstract class ShaftBlockEntity extends BlockEntity {
    protected final ResourceLocation id;
    protected MechanicalNode node;

    public final List<QuadGeometryData> north = new ArrayList<>();
    public final List<QuadGeometryData> south = new ArrayList<>();
    public final List<QuadGeometryData> east  = new ArrayList<>();
    public final List<QuadGeometryData> west  = new ArrayList<>();
    public final List<QuadGeometryData> up    = new ArrayList<>();
    public final List<QuadGeometryData> down  = new ArrayList<>();

    public ShaftBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        super(TechcoreBlockEntities.BLOCK_ENTITIES.get(id).value(), pos, state);
        this.id = id;
    }

    public abstract ShaftConfig getConfig();

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (!level.isClientSide) {
            if (this.node == null) {
                this.node = new MechanicalNode(level, this.getBlockPos(), getConfig());
                MechanicalGraphManager.get().registerNode(level, this.node);
            }
        }

        this.setupModel(this);
    }

    private <BE extends ShaftBlockEntity> void setupModel(BE blockEntity) {
        Class<? extends ShaftConfig> configClass = TechcoreMechanicalPartRegistry.getRegisteredShaftConfig(this.id);
        ShaftConfig config;
        try {
            config = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate ShaftConfig for " + id, e);
        }

        List<QuadGeometryData> allQuads = ModelBuilder.buildShaftModel(config);
        ModelBuilder.classifyQuadsByDirection(
                allQuads,
                blockEntity.north,
                blockEntity.south,
                blockEntity.east,
                blockEntity.west,
                blockEntity.up,
                blockEntity.down
        );
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        if (be instanceof ShaftBlockEntity shaft && !level.isClientSide && shaft.node != null) {
            shaft.node.tick();
        }
    }
}