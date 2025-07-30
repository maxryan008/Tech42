package dev.maximus.techcore.client.models;

import dev.maximus.techcore.api.mechanical.MechanicalPartModelDefinition;
import dev.maximus.techcore.api.mechanical.TechcoreMechanicalPartRegistry;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class GearUnbakedModel implements UnbakedModel {
    private final ResourceLocation id;

    public GearUnbakedModel(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> function) {

    }

    @Override
    public @Nullable BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState) {
        MechanicalPartModelDefinition def = TechcoreMechanicalPartRegistry.getGearModelDefinition(id);

        return new GearBakedModel(function.apply(new Material(TextureAtlas.LOCATION_BLOCKS, def.textureLocation())), modelState);
    }
}
