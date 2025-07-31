package dev.maximus.techcore.api.mechanical;

import dev.maximus.techcore.TechcoreBlockEntities;
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
    protected float yaw = 0.0f;
    private float prevYaw = 0.0f;

    public final List<QuadGeometryData> north = new ArrayList<>();
    public final List<QuadGeometryData> south = new ArrayList<>();
    public final List<QuadGeometryData> east  = new ArrayList<>();
    public final List<QuadGeometryData> west  = new ArrayList<>();
    public final List<QuadGeometryData> up    = new ArrayList<>();
    public final List<QuadGeometryData> down  = new ArrayList<>();

    public GearBlockEntity(BlockPos pos, BlockState state, ResourceLocation id) {
        super(TechcoreBlockEntities.BLOCK_ENTITIES.get(id).value(), pos, state);
        this.id = id;
    }

    protected static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (blockEntity instanceof GearBlockEntity gearBlockEntity) {
            gearBlockEntity.serverTick(level, pos, state, gearBlockEntity);
        }
    }

    public abstract void serverTick(Level level, BlockPos pos, BlockState state, GearBlockEntity blockEntity);

    public abstract <BE extends GearBlockEntity> void init(BE blockEntity);

    public ResourceLocation getMultiblockId() {
        return this.id;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        this.setup(this);
        if (!level.isClientSide()) {
            this.init(this);
        }
    }

    private <BE extends GearBlockEntity> void setup(BE blockEntity) {
        Class<? extends GearConfig> configClass = TechcoreMechanicalPartRegistry.getRegisteredGearConfig(blockEntity.id);
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

    public float getYaw() {
        return this.yaw;
    }

    public float getPrevYaw() {
        return this.prevYaw;
    }

    public void setYaw(float yaw) {
        if (this.level != null && this.level.isClientSide) {
            this.prevYaw = this.yaw;
        }
        this.yaw = yaw;

        assert this.level != null;
        if (!this.level.isClientSide) {
            this.setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void addYaw(float yaw) {
        if (this.level != null && this.level.isClientSide) {
            this.prevYaw = this.yaw;
        }
        this.yaw += yaw;

        assert this.level != null;
        if (!this.level.isClientSide) {
            this.setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public @Nullable Object getRenderData() {
        return this;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.yaw = compoundTag.getFloat("Yaw");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putFloat("Yaw", this.yaw);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}