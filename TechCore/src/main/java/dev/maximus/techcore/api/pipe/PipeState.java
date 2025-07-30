package dev.maximus.techcore.api.pipe;

import dev.maximus.techcore.api.substance.Substance;

import java.util.Map;

public final class PipeState {
    private float pressure = 1.0f;
    private float temperature = 298.15f;

    // Map of Substance to volume ratio (0.0–1.0)
    private final Map<Substance, Float> contents;

    public PipeState(Map<Substance, Float> contents) {
        this.contents = contents;
    }

    public float getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public Map<Substance, Float> getContents() {
        return contents;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setContent(Substance substance, float ratio) {
        contents.put(substance, ratio);
    }

    public void removeSubstance(Substance substance) {
        contents.remove(substance);
    }
}