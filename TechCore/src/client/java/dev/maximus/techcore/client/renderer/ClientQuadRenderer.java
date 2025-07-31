package dev.maximus.techcore.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.maximus.techcore.model.QuadGeometryData;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ClientQuadRenderer {
    public static void emit(QuadGeometryData quad, VertexConsumer buffer, Matrix4f pose, int light, int overlay, TextureAtlasSprite sprite, int dx, int dy, int dz) {
        Vector3f[] vertices = new Vector3f[] { quad.v1(), quad.v2(), quad.v3(), quad.v4() };

        for (int i = 0; i < 4; i++) {
            Vector3f vec = new Vector3f(vertices[i]);
            vec.mulPosition(pose);

            float u = (i == 0 || i == 3) ? sprite.getU(0) : sprite.getU(1);
            float v = (i < 2) ? sprite.getV(0) : sprite.getV(1);

            buffer.addVertex(vec.x, vec.y, vec.z)
                    .setColor(255, 255, 255, 255)
                    .setUv(u, v)
                    .setLight(light)
                    .setNormal(dx, dy, dz)
                    .setOverlay(overlay);
        }
    }
}
