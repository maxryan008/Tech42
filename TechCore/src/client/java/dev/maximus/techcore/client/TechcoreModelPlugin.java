package dev.maximus.techcore.client;

import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import dev.maximus.techcore.client.models.GearUnbakedModel;
import dev.maximus.techcore.client.models.ShaftUnbakedModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TechcoreModelPlugin implements ModelLoadingPlugin {
    public static final TechcoreModelPlugin INSTANCE = new TechcoreModelPlugin();

    @Override
    public void onInitializeModelLoader(ModelLoadingPlugin.Context pluginContext) {
        // Register model IDs (block + item)
        pluginContext.addModels(TechcoreMechanicalPartRegistry.getAllGearModelIds());
        pluginContext.addModels(TechcoreMechanicalPartRegistry.getAllGearItemModelIds());
        pluginContext.addModels(TechcoreMechanicalPartRegistry.getAllShaftModelIds());
        pluginContext.addModels(TechcoreMechanicalPartRegistry.getAllShaftItemModelIds());

        // Register model resolver for both block and item
        pluginContext.resolveModel().register(context -> {
            ResourceLocation id = context.id();

            if (TechcoreMechanicalPartRegistry.hasGearModel(id)) {
                return new GearUnbakedModel(id);
            }

            // Item models redirect to their block version
            if (TechcoreMechanicalPartRegistry.hasGearModel(itemToBlock(id))) {
                return new GearUnbakedModel(itemToBlock(id));
            }

            if (TechcoreMechanicalPartRegistry.hasShaftModel(id)) {
                return new ShaftUnbakedModel(id);
            }

            // Item models redirect to their block version
            if (TechcoreMechanicalPartRegistry.hasShaftModel(itemToBlock(id))) {
                return new ShaftUnbakedModel(itemToBlock(id));
            }

            return null;
        });

        // Register gear blockstate resolvers
        for (Block block : TechcoreMechanicalPartRegistry.getRegisteredGearBlocks()) {
            pluginContext.registerBlockStateResolver(block, blockContext -> {
                for (BlockState state : blockContext.block().getStateDefinition().getPossibleStates()) {
                    ResourceLocation id = TechcoreMechanicalPartRegistry.getModelId(blockContext.block());
                    blockContext.setModel(state, new GearUnbakedModel(id));
                }
            });
        }

        // Register shaft blockstate resolver
        for (Block block : TechcoreMechanicalPartRegistry.getRegisteredShaftBlocks()) {
            pluginContext.registerBlockStateResolver(block, blockContext -> {
                for (BlockState state : blockContext.block().getStateDefinition().getPossibleStates()) {
                    ResourceLocation id = TechcoreMechanicalPartRegistry.getModelId(blockContext.block());
                    blockContext.setModel(state, new ShaftUnbakedModel(id));
                }
            });
        }
    }

    private ResourceLocation itemToBlock(ResourceLocation id) {
        // Convert item/custom_block -> block/custom_block
        if (id.getPath().startsWith("item/")) {
            return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().replace("item/", "block/"));
        }
        return id;
    }
}
