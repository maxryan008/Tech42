package dev.maximus.techcore.model;

import dev.maximus.techcore.api.mechanical.gear.GearConfig;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ModelBuilder {
    public static List<QuadGeometryData> boxRotated(float x1, float y1, float z1, float x2, float y2, float z2, Quaternionf rotation) {
        List<QuadGeometryData> quads = new ArrayList<>();
        Vector3f center = new Vector3f(
                (x1 + x2) / 2.0f,
                (y1 + y2) / 2.0f,
                (z1 + z2) / 2.0f
        );

        for (QuadGeometryData quad : box(x1, y1, z1, x2, y2, z2)) {
            quads.add(quad.rotate(rotation, center));
        }

        return quads;
    }

    public static List<QuadGeometryData> box(float x1, float y1, float z1, float x2, float y2, float z2) {
        List<QuadGeometryData> quads = new ArrayList<>();

        float minX = Math.min(x1, x2);
        float maxX = Math.max(x1, x2);
        float minY = Math.min(y1, y2);
        float maxY = Math.max(y1, y2);
        float minZ = Math.min(z1, z2);
        float maxZ = Math.max(z1, z2);

        // DOWN
        quads.add(new QuadGeometryData(
                new Vector3f(minX, minY, minZ),
                new Vector3f(maxX, minY, minZ),
                new Vector3f(maxX, minY, maxZ),
                new Vector3f(minX, minY, maxZ)
        ));
        // UP
        quads.add(new QuadGeometryData(
                new Vector3f(minX, maxY, maxZ),
                new Vector3f(maxX, maxY, maxZ),
                new Vector3f(maxX, maxY, minZ),
                new Vector3f(minX, maxY, minZ)
        ));
        // NORTH
        quads.add(new QuadGeometryData(
                new Vector3f(minX, maxY, minZ),
                new Vector3f(maxX, maxY, minZ),
                new Vector3f(maxX, minY, minZ),
                new Vector3f(minX, minY, minZ)
        ));
        // SOUTH
        quads.add(new QuadGeometryData(
                new Vector3f(minX, minY, maxZ),
                new Vector3f(maxX, minY, maxZ),
                new Vector3f(maxX, maxY, maxZ),
                new Vector3f(minX, maxY, maxZ)
        ));
        // WEST
        quads.add(new QuadGeometryData(
                new Vector3f(minX, maxY, maxZ),
                new Vector3f(minX, maxY, minZ),
                new Vector3f(minX, minY, minZ),
                new Vector3f(minX, minY, maxZ)
        ));
        // EAST
        quads.add(new QuadGeometryData(
                new Vector3f(maxX, maxY, minZ),
                new Vector3f(maxX, maxY, maxZ),
                new Vector3f(maxX, minY, maxZ),
                new Vector3f(maxX, minY, minZ)
        ));

        return quads;
    }

    public static List<QuadGeometryData> buildGearModel(GearConfig config) {
        float toothLength = config.getToothLength();
        float gearHalfWidth = config.getGearWidth() / 2f;
        List<QuadGeometryData> quads = box(toothLength, 0.5f - gearHalfWidth, toothLength, 1 - toothLength, 0.5f + gearHalfWidth, 1 - toothLength);
        float toothAngle = 360f / config.getToothCount();
        float toothHalfHeight = config.getToothHeight() / 2f;
        float toothY1 = 0.5f - toothHalfHeight;
        float toothY2 = 0.5f + toothHalfHeight;

            for (int i = 0; i < config.getToothCount(); i++) {
                float angleDeg = i * toothAngle;
                float angleRad = (float) Math.toRadians(angleDeg);

                float dx = (float) Math.cos(angleRad);
                float dz = (float) Math.sin(angleRad);
                float protrusion = 0.01f;

                float radialCenter = config.getGearRadius() + config.getToothLength() / 2f + protrusion;
                float centerX = 0.5f + dx * radialCenter;
                float centerZ = 0.5f + dz * radialCenter;

                float halfLength = config.getToothLength();
                float halfWidth = config.getToothWidth();

                Quaternionf rotation = new Quaternionf().rotateY(-angleRad);

                quads.addAll(boxRotated(
                        -halfLength + centerX, toothY1, -halfWidth + centerZ,
                        halfLength + centerX, toothY2, halfWidth + centerZ,
                        rotation
                ));
            }

            return quads;
    }

    public static void classifyQuadsByDirection(
            List<QuadGeometryData> input,
            List<QuadGeometryData> north,
            List<QuadGeometryData> south,
            List<QuadGeometryData> east,
            List<QuadGeometryData> west,
            List<QuadGeometryData> up,
            List<QuadGeometryData> down
    ) {
        // Define direction unit vectors
        Vector3f[] directions = {
                new Vector3f(0, 0, -1), // NORTH
                new Vector3f(0, 0,  1), // SOUTH
                new Vector3f(1, 0,  0), // EAST
                new Vector3f(-1, 0, 0), // WEST
                new Vector3f(0, 1,  0), // UP
                new Vector3f(0, -1, 0)  // DOWN
        };

        List<QuadGeometryData>[] outputs = new List[] { north, south, east, west, up, down };

        for (QuadGeometryData quad : input) {
            Vector3f edge1 = new Vector3f(quad.v2()).sub(quad.v1());
            Vector3f edge2 = new Vector3f(quad.v3()).sub(quad.v1());
            Vector3f normal = edge1.cross(edge2).normalize();

            // Find the direction with the highest dot product (closest alignment)
            float bestDot = -Float.MAX_VALUE;
            int bestIndex = -1;

            for (int i = 0; i < directions.length; i++) {
                float dot = normal.dot(directions[i]);
                if (dot > bestDot) {
                    bestDot = dot;
                    bestIndex = i;
                }
            }

            if (bestIndex != -1) {
                outputs[bestIndex].add(quad);
            }
        }
    }
}
