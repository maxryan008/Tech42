package dev.maximus.techcore.api.mechanical;

public interface MechanicalNode {
    void setNetwork(MechanicalNetwork network);
    MechanicalNetwork getNetwork();

    float getRpm();
    float getTorque();
    void applyLoad(float rpm, float torque);
}