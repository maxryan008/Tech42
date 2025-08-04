package dev.maximus.techcore.client;

import dev.maximus.techcore.Constant;
import dev.maximus.techcore.TechcoreBlockEntities;
import dev.maximus.techcore.api.mechanical.gear.GearBlockEntity;
import dev.maximus.techcore.api.mechanical.gear.GearConfig;
import dev.maximus.techcore.api.mechanical.MechanicalPartModelDefinition;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import dev.maximus.techcore.client.renderer.GearBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TechcoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Constant.LOGGER.info("Initializing Techcore Client");
        ModelLoadingPlugin.register(TechcoreModelPlugin.INSTANCE);
        for (Block block : TechcoreMechanicalPartRegistry.getRegisteredGearBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }
        for (Block block : TechcoreMechanicalPartRegistry.getRegisteredShaftBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        }

        for (Holder.Reference<BlockEntityType<?>> holder : TechcoreBlockEntities.BLOCK_ENTITIES.getEntries()) {
            ResourceLocation id = holder.key().location();

            if (!TechcoreMechanicalPartRegistry.hasGearModel(id)) continue;

            MechanicalPartModelDefinition def = TechcoreMechanicalPartRegistry.getGearModelDefinition(id);
            Class<? extends GearConfig> configClass = TechcoreMechanicalPartRegistry.getRegisteredGearConfig(id);
            BlockEntityType<?> type = holder.value();

            @SuppressWarnings("unchecked")
            BlockEntityType<GearBlockEntity> castedType = (BlockEntityType<GearBlockEntity>) type;

            BlockEntityRenderers.register(castedType,ctx ->
                    new GearBlockEntityRenderer(def.textureLocation())
            );
        }
    }
}
