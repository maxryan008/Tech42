package dev.maximus.techcore.api.mechanical;

import dev.maximus.techcore.TechcoreBlockEntities;
import dev.maximus.techcore.TechcoreBlocks;
import dev.maximus.techcore.TechcoreItems;
import dev.maximus.techcore.api.mechanical.gear.GearBlock;
import dev.maximus.techcore.api.mechanical.gear.GearBlockEntity;
import dev.maximus.techcore.api.mechanical.gear.GearConfig;
import dev.maximus.techcore.api.mechanical.shaft.ShaftBlock;
import dev.maximus.techcore.api.mechanical.shaft.ShaftBlockEntity;
import dev.maximus.techcore.api.mechanical.shaft.ShaftConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TechcoreMechanicalPartRegistry {
    private static final Map<ResourceLocation, MechanicalPartModelDefinition> REGISTERED_GEAR_MODELS = new HashMap<>();
    private static final Map<ResourceLocation, MechanicalPartModelDefinition> REGISTERED_SHAFT_MODELS = new HashMap<>();
    private static final Map<ResourceLocation, Block> REGISTERED_GEAR_BLOCKS = new HashMap<>();
    private static final Map<ResourceLocation, Block> REGISTERED_SHAFT_BLOCKS = new HashMap<>();
    private static final Map<Block, ResourceLocation> BLOCK_TO_MODEL_ID = new HashMap<>();

    private static final Map<ResourceLocation, Class<? extends GearConfig>> REGISTERED_GEAR_CONFIGS = new HashMap<>();
    private static final Map<ResourceLocation, Class<? extends ShaftConfig>> REGISTERED_SHAFT_CONFIGS = new HashMap<>();

    public static <GC extends GearConfig, BE extends GearBlockEntity>void registerGear(
        ResourceLocation id,
        Class<GC> gearClass,
        Class<BE> blockEntityClass,
        ResourceLocation textureLocation
    ) {
        MechanicalPartModelDefinition modelDefinition = new MechanicalPartModelDefinition(
                textureLocation
        );

        // Model IDs
        ResourceLocation blockModelId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
        ResourceLocation itemModelId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + id.getPath());

        // Reflective block entity supplier
        BlockEntityType.BlockEntitySupplier<BE> supplier = (pos, state) -> {
            try {
                Constructor<BE> constructor = blockEntityClass.getConstructor(BlockPos.class, BlockState.class);
                return constructor.newInstance(pos, state);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate BlockEntity class '" + blockEntityClass.getName()
                        + "'. It must have a public constructor (BlockPos, BlockState).", e);
            }
        };

        // Create block instance
        GearBlock block = new GearBlock(
                Block.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(),
                (pos, state) -> supplier.create((BlockPos) pos, (BlockState) state)
        );

        // Register block and item
        TechcoreBlocks.registerBlock(id, block);
        TechcoreItems.registerBlockItem(id, block);

        // Register block entity type
        BlockEntityType<BE> blockEntityType = BlockEntityType.Builder.of(supplier, block).build(null);
        TechcoreBlockEntities.BLOCK_ENTITIES.register(id, blockEntityType);

        REGISTERED_GEAR_CONFIGS.put(blockModelId, gearClass);
        REGISTERED_GEAR_MODELS.put(blockModelId, modelDefinition);
        REGISTERED_GEAR_MODELS.put(itemModelId, modelDefinition);
        BLOCK_TO_MODEL_ID.put(block, blockModelId);
        REGISTERED_GEAR_BLOCKS.put(blockModelId, block);
    }

    public static <SC extends ShaftConfig, BE extends ShaftBlockEntity>void registerShaft(
            ResourceLocation id,
            Class<SC> shaftConfigClass,
            Class<BE> blockEntityClass,
            ResourceLocation textureLocation
    ) {
        MechanicalPartModelDefinition modelDefinition = new MechanicalPartModelDefinition(
                textureLocation
        );

        // Model IDs
        ResourceLocation blockModelId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
        ResourceLocation itemModelId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/" + id.getPath());

        // Reflective block entity supplier
        BlockEntityType.BlockEntitySupplier<BE> supplier = (pos, state) -> {
            try {
                Constructor<BE> constructor = blockEntityClass.getConstructor(BlockPos.class, BlockState.class);
                return constructor.newInstance(pos, state);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate BlockEntity class '" + blockEntityClass.getName()
                        + "'. It must have a public constructor (BlockPos, BlockState).", e);
            }
        };

        // Create block instance
        ShaftBlock block = new ShaftBlock(
                Block.Properties.ofFullCopy(Blocks.IRON_BLOCK),
                (pos, state) -> supplier.create((BlockPos) pos, (BlockState) state)
        );

        // Register block and item
        TechcoreBlocks.registerBlock(id, block);
        TechcoreItems.registerBlockItem(id, block);

        // Register block entity type
        BlockEntityType<BE> blockEntityType = BlockEntityType.Builder.of(supplier, block).build(null);
        TechcoreBlockEntities.BLOCK_ENTITIES.register(id, blockEntityType);

        REGISTERED_SHAFT_CONFIGS.put(blockModelId, shaftConfigClass);
        REGISTERED_SHAFT_MODELS.put(blockModelId, modelDefinition);
        REGISTERED_SHAFT_MODELS.put(itemModelId, modelDefinition);
        BLOCK_TO_MODEL_ID.put(block, blockModelId);
        REGISTERED_SHAFT_BLOCKS.put(blockModelId, block);
    }

    public static boolean hasGearModel(ResourceLocation id) {
        if (!id.getPath().startsWith("block/")) {
            id = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
        }
        return REGISTERED_GEAR_MODELS.containsKey(id);
    }
    public static boolean hasShaftModel(ResourceLocation id) {
        return REGISTERED_SHAFT_MODELS.containsKey(id);
    }

    public static Block getRegisteredGearBlock(ResourceLocation typeId) {
        // If the typeId isn't already prefixed with "block/", try to remap
        if (!typeId.getPath().startsWith("block/")) {
            typeId = ResourceLocation.fromNamespaceAndPath(typeId.getNamespace(), "block/" + typeId.getPath());
        }
        return REGISTERED_GEAR_BLOCKS.get(typeId);
    }

    public static Block getRegisteredShaftBlock(ResourceLocation typeId) {
        // If the typeId isn't already prefixed with "block/", try to remap
        if (!typeId.getPath().startsWith("block/")) {
            typeId = ResourceLocation.fromNamespaceAndPath(typeId.getNamespace(), "block/" + typeId.getPath());
        }
        return REGISTERED_SHAFT_BLOCKS.get(typeId);
    }

    public static MechanicalPartModelDefinition getGearModelDefinition(ResourceLocation id) {
        if (!id.getPath().startsWith("block/")) {
            id = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
        }
        return REGISTERED_GEAR_MODELS.get(id);
    }

    public static MechanicalPartModelDefinition getShaftModelDefinition(ResourceLocation id) {
        if (!id.getPath().startsWith("block/")) {
            id = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
        }
        return REGISTERED_SHAFT_MODELS.get(id);
    }

    public static Collection<ResourceLocation> getAllGearModelIds() {
        return REGISTERED_GEAR_MODELS.keySet();
    }
    public static Collection<ResourceLocation> getAllShaftModelIds() {
        return REGISTERED_SHAFT_MODELS.keySet();
    }

    public static Collection<ResourceLocation> getAllGearItemModelIds() {
        return REGISTERED_GEAR_MODELS.keySet().stream()
                .filter(id -> id.getPath().startsWith("item/"))
                .toList();
    }

    public static Collection<ResourceLocation> getAllShaftItemModelIds() {
        return REGISTERED_SHAFT_MODELS.keySet().stream()
                .filter(id -> id.getPath().startsWith("item/"))
                .toList();
    }

    public static Collection<Block> getRegisteredGearBlocks() {
        return REGISTERED_GEAR_BLOCKS.values();
    }
    public static Collection<Block> getRegisteredShaftBlocks() {
        return REGISTERED_SHAFT_BLOCKS.values();
    }

    public static ResourceLocation getModelId(Block block) {
        return BLOCK_TO_MODEL_ID.get(block);
    }

    public static Class<? extends GearConfig> getRegisteredGearConfig(ResourceLocation modelId) {
        if (!modelId.getPath().startsWith("block/")) {
            modelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), "block/" + modelId.getPath());
        }
        return REGISTERED_GEAR_CONFIGS.get(modelId);
    }

    public static Class<? extends ShaftConfig> getRegisteredShaftConfig(ResourceLocation modelId) {
        if (!modelId.getPath().startsWith("block/")) {
            modelId = ResourceLocation.fromNamespaceAndPath(modelId.getNamespace(), "block/" + modelId.getPath());
        }
        return REGISTERED_SHAFT_CONFIGS.get(modelId);
    }
}