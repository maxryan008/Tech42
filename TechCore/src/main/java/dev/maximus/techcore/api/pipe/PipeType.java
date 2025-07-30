package dev.maximus.techcore.api.pipe;

import dev.maximus.techcore.api.substance.PhaseState;

public interface PipeType {
    boolean supportsPhase(PhaseState phase);

    float getMinOperatingTemp(); // K
    float getMaxOperatingTemp(); // K

    float getMinPressure(); // atm
    float getMaxPressure(); // atm

    float getThermalConductivity(); // W/m·K

    /**
     * Called when an incompatible phase enters the pipe.
     */
    default void onPhaseIncompatibility(PipeContext context, PhaseState phase) {}

    /**
     * Called when a substance exceeds safe pressure/temp.
     */
    default void onPipeFailure(PipeContext context, String reason) {}

    /**
     * Determines whether the pipe is insulated.
     */
    default boolean isInsulated() {
        return false;
    }
}