package dev.maximus.techcore.model;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class QuadGeometryData {
    private final Vector3f v1, v2, v3, v4;

    public QuadGeometryData(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    public Vector3f v1() { return v1; }
    public Vector3f v2() { return v2; }
    public Vector3f v3() { return v3; }
    public Vector3f v4() { return v4; }

    public List<Vector3f> corners() {
        return List.of(v1, v2, v3, v4);
    }

    public QuadGeometryData rotate(Quaternionf rot, Vector3f around) {
        return new QuadGeometryData(
                new Vector3f(v1()).sub(around).rotate(rot).add(around),
                new Vector3f(v2()).sub(around).rotate(rot).add(around),
                new Vector3f(v3()).sub(around).rotate(rot).add(around),
                new Vector3f(v4()).sub(around).rotate(rot).add(around)
        );
    }
}