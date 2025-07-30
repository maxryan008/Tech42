package dev.maximus.tech42.pipe;

import dev.maximus.techcore.api.pipe.PipeContext;
import dev.maximus.techcore.api.pipe.PipeType;
import dev.maximus.techcore.api.substance.PhaseState;

public class CopperPipeType implements PipeType {
    @Override
    public boolean supportsPhase(PhaseState phase) {
        // Only supports liquid and gas, not solid
        return phase == PhaseState.LIQUID || phase == PhaseState.GAS;
    }

    @Override
    public float getMinOperatingTemp() {
        return 273.15f; // 0°C
    }

    @Override
    public float getMaxOperatingTemp() {
        return 573.15f; // 300°C
    }

    @Override
    public float getMinPressure() {
        return 0.1f; // Vacuum tolerance
    }

    @Override
    public float getMaxPressure() {
        return 20.0f; // 20 atm max
    }

    @Override
    public float getThermalConductivity() {
        return 401.0f; // Copper (W/m·K)
    }

    @Override
    public boolean isInsulated() {
        return false;
    }

    @Override
    public void onPhaseIncompatibility(PipeContext context, PhaseState phase) {
        // Example: clog pipe and lower pressure
        context.state().setPressure(context.state().getPressure() * 0.5f);
        // You might also mark the block visually as "clogged" or "frozen"
    }

    @Override
    public void onPipeFailure(PipeContext context, String reason) {
        // Drop items, explode, or just log for now
        System.out.println("Copper pipe at " + context.pos() + " failed: " + reason);
    }
}