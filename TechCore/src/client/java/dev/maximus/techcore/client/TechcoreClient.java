package dev.maximus.techcore.client;

import dev.maximus.techcore.Constant;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

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
    }
}
