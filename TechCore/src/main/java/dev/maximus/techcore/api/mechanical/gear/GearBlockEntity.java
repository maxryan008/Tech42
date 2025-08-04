package dev.maximus.techcore.api.mechanical.gear;

import dev.maximus.techcore.TechcoreBlockEntities;
import dev.maximus.techcore.api.mechanical.MechanicalNode;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import dev.maximus.techcore.mechanical.graph.MechanicalGraphManager;
import dev.maximus.techcore.model.ModelBuilder;
import dev.maximus.techcore.model.QuadGeometryData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class GearBlockEntity extends BlockEntity {
    protected final ResourceLocation id;

    // Rotation state for rendering only
    private float yaw = 0.0f;
    private float prevYaw = 0.0f;
    private boolean reversed = false;

    public final List<QuadGeometryData> north = new ArrayList<>();
    public final List<QuadGeometryData> south = new ArrayList<>();
    public final List<QuadGeometryData> east  = new ArrayList<>();
    public final List<QuadGeometryData> west  = new ArrayList<>();
    public final List<QuadGeometryData> up    = new ArrayList<>();
    public final List<QuadGeometryData> down  = new ArrayList<>();

    protected MechanicalNode node;

    public GearBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        super(TechcoreBlockEntities.BLOCK_ENTITIES.get(id).value(), pos, state);
        this.id = id;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (!level.isClientSide()) {
            this.initNode(level);
        }
        this.setupModel(this);
    }

    private <BE extends GearBlockEntity> void setupModel(BE blockEntity) {
        Class<? extends GearConfig> configClass = TechcoreMechanicalPartRegistry.getRegisteredGearConfig(this.id);
        GearConfig config;
        try {
            config = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate GearConfig for " + id, e);
        }

        List<QuadGeometryData> allQuads = ModelBuilder.buildGearModel(config);
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

    private void initNode(Level level) {
        if (this.node == null) {
            this.node = new MechanicalNode(level, this.getBlockPos(), this.getConfig());
            MechanicalGraphManager.get().registerNode(level, this.node);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null && !this.level.isClientSide() && this.node != null) {
            MechanicalGraphManager.get().unregisterNode(this.getBlockPos());
        }
    }

    public abstract GearConfig getConfig();

    public float getYaw() {
        return this.yaw;
    }

    public float getPrevYaw() {
        return this.prevYaw;
    }

    public void clientTick() {
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        if (be instanceof GearBlockEntity gear) {
            if (!level.isClientSide) {
                if (gear.node != null) {
                    gear.prevYaw = gear.yaw;
                    gear.yaw = gear.node.yaw + gear.node.yawOffset;
                    gear.reversed = gear.node.speedRatio == -1;
                    gear.setChanged();
                    gear.level.sendBlockUpdated(gear.worldPosition, gear.getBlockState(), gear.getBlockState(), 3);
                }
            } else {
                gear.clientTick();
            }
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.yaw = tag.getFloat("Yaw");
        this.prevYaw = tag.getFloat("PrevYaw");
        this.reversed = tag.getBoolean("Reversed");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putFloat("Yaw", this.yaw);
        tag.putFloat("PrevYaw", this.prevYaw);
        tag.putBoolean("Reversed", this.reversed);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @Nullable Object getRenderData() {
        return this;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}