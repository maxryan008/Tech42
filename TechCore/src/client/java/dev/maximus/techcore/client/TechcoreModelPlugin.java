package dev.maximus.techcore.client;

import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import dev.maximus.techcore.client.models.GearUnbakedModel;
import dev.maximus.techcore.client.models.ShaftUnbakedModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TechcoreModelPlugin implements ModelLoadingPlugin {
    public static final TechcoreModelPlugin INSTANCE = new TechcoreModelPlugin();

    @Override
    public void onInitializeModelLoader(Context context) {
        context.addModels(TechcoreMechanicalPartRegistry.getAllGearModelIds());
        context.addModels(TechcoreMechanicalPartRegistry.getAllGearItemModelIds());
        context.addModels(TechcoreMechanicalPartRegistry.getAllShaftModelIds());
        context.addModels(TechcoreMechanicalPartRegistry.getAllShaftItemModelIds());

        context.resolveModel().register(modelContext -> {
            ResourceLocation id = modelContext.id();
            ResourceLocation blockId = toBlockModelId(id);

            if (TechcoreMechanicalPartRegistry.hasGearModel(blockId)) {
                return new GearUnbakedModel(blockId);
            } else if (TechcoreMechanicalPartRegistry.hasShaftModel(blockId)) {
                return new ShaftUnbakedModel(blockId);
            }
            return null;
        });

        registerBlockStates(context, TechcoreMechanicalPartRegistry.getRegisteredGearBlocks(), GearUnbakedModel::new);
        registerBlockStates(context, TechcoreMechanicalPartRegistry.getRegisteredShaftBlocks(), ShaftUnbakedModel::new);
    }

    private void registerBlockStates(Context context, Iterable<Block> blocks, java.util.function.Function<ResourceLocation, UnbakedModel> factory) {
        for (Block block : blocks) {
            ResourceLocation id = TechcoreMechanicalPartRegistry.getModelId(block);
            context.registerBlockStateResolver(block, blockCtx -> {
                for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                    blockCtx.setModel(state, factory.apply(id));
                }
            });
        }
    }

    private ResourceLocation toBlockModelId(ResourceLocation id) {
        if (id.getPath().startsWith("item/")) {
            return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring(5).replaceFirst("^", "block/"));
        }
        return id;
    }
}