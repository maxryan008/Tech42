package dev.maximus.techcore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.maximus.techcore.api.mechanical.gear.GearBlockEntity;
import dev.maximus.techcore.model.QuadGeometryData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

import java.util.List;

public class GearBlockEntityRenderer implements BlockEntityRenderer<GearBlockEntity> {
    private final TextureAtlasSprite sprite;

    public GearBlockEntityRenderer(ResourceLocation texture) {
        this.sprite = Minecraft.getInstance()
                .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(texture);
    }

    @Override
    public void render(GearBlockEntity entity, float ignored, PoseStack matrices,
                       MultiBufferSource vertexConsumers, int light, int overlay) {
        float tickDelta = Minecraft.getInstance().getTimer().getRealtimeDeltaTicks(); // Use this instead

        float yaw = entity.getYaw();
        float prevYaw = entity.getPrevYaw();
        boolean reversed = entity.isReversed();

        if (reversed) {
            if (yaw - prevYaw > 0) {
                prevYaw += 360;
            }
        } else {
            if (prevYaw - yaw > 0) {
                prevYaw -= 360;
            }
        }


//        if (yaw < 100 && prevYaw > 260) prevYaw -= 360;
//        else if (yaw > 260 && prevYaw < 100) prevYaw += 360;

        float interpolatedYaw = prevYaw + (yaw - prevYaw) * tickDelta;

        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.mulPose(new Quaternionf().rotateY((float) Math.toRadians(interpolatedYaw)));
        matrices.translate(-0.5, -0.5, -0.5);

        VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.cutout());

        renderList(Direction.NORTH, entity.north, buffer, matrices, light, overlay);
        renderList(Direction.SOUTH, entity.south, buffer, matrices, light, overlay);
        renderList(Direction.EAST, entity.east, buffer, matrices, light, overlay);
        renderList(Direction.WEST, entity.west, buffer, matrices, light, overlay);
        renderList(Direction.UP, entity.up, buffer, matrices, light, overlay);
        renderList(Direction.DOWN, entity.down, buffer, matrices, light, overlay);

        matrices.popPose();
    }

    private void renderList(Direction dir, List<QuadGeometryData> quads, VertexConsumer buffer, PoseStack matrices, int light, int overlay) {
        Vec3i normal = dir.getNormal();
        for (QuadGeometryData quad : quads) {
            ClientQuadRenderer.emit(quad, buffer, matrices.last().pose(), light, overlay, sprite, normal.getX(), normal.getY(), normal.getZ());
        }
    }
}