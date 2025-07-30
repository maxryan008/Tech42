package dev.maximus.techcore.client.models;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public class ShaftBakedModel implements BakedModel {
    private final TextureAtlasSprite sprite;
    private final ModelState state;

    public ShaftBakedModel(TextureAtlasSprite sprite, ModelState state) {
        this.sprite = sprite;
        this.state = state;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        BakedModel.super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        BakedModel.super.emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public boolean isGui3d() { return true; }
    @Override public boolean usesBlockLight() { return true; }
    @Override public boolean isCustomRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleIcon() { return this.sprite; }
    @Override public ItemOverrides getOverrides() { return ItemOverrides.EMPTY; }

    @Override
    public ItemTransforms getTransforms() {
        return new ItemTransforms(
                // third person left
                new ItemTransform(new Vector3f(75f, 45f, 0f), new Vector3f(0f, 0.2f, -0.02f), new Vector3f(0.375f, 0.375f, 0.375f)),
                // third person right
                new ItemTransform(new Vector3f(75f, 45f, 0f), new Vector3f(0f, 0.2f, -0.02f), new Vector3f(0.375f, 0.375f, 0.375f)),
                // first person left
                new ItemTransform(new Vector3f(0f, 45f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(0.4f, 0.4f, 0.4f)),
                // first person right
                new ItemTransform(new Vector3f(0f, 45f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(0.4f, 0.4f, 0.4f)),
                // head
                new ItemTransform(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f)),
                // GUI
                new ItemTransform(new Vector3f(30f, 225f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(0.625f, 0.625f, 0.625f)),
                // ground
                new ItemTransform(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0.2f, 0f), new Vector3f(0.25f, 0.25f, 0.25f)),
                // fixed
                new ItemTransform(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(0.5f, 0.5f, 0.5f))
        );
    }
}
