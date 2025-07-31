package dev.maximus.techcore.client.models;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Supplier;

public class ShaftBakedModel implements BakedModel {
    private final TextureAtlasSprite sprite;

    public ShaftBakedModel(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> random, RenderContext context) {}

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> random, RenderContext context) {}

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, @NotNull RandomSource random) {
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
                new ItemTransform(new Vector3f(75f, 45f, 0f), new Vector3f(0f, 0.2f, -0.02f), new Vector3f(0.375f)),
                new ItemTransform(new Vector3f(75f, 45f, 0f), new Vector3f(0f, 0.2f, -0.02f), new Vector3f(0.375f)),
                new ItemTransform(new Vector3f(0f, 45f, 0f), new Vector3f(0, 0, 0), new Vector3f(0.4f)),
                new ItemTransform(new Vector3f(0f, 45f, 0f), new Vector3f(0, 0, 0), new Vector3f(0.4f)),
                ItemTransform.NO_TRANSFORM,
                new ItemTransform(new Vector3f(30f, 225f, 0f), new Vector3f(0, 0, 0), new Vector3f(0.625f)),
                new ItemTransform(new Vector3f(0, 0, 0), new Vector3f(0f, 0.2f, 0f), new Vector3f(0.25f)),
                new ItemTransform(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0.5f))
        );
    }
}
