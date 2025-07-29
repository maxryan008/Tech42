package dev.maximus.techcore.api.gas;

public class GasStack {
    private final Gas gas;
    private int amount;          // mB (milliBuckets)
    private float pressure;      // kPa or arbitrary units
    private float temperature;   // K

    public GasStack(Gas gas, int amount, float pressure, float temperature) {
        this.gas = gas;
        this.amount = amount;
        this.pressure = pressure;
        this.temperature = temperature;
    }

    public Gas getGas() { return gas; }
    public int getAmount() { return amount; }
    public float getPressure() { return pressure; }
    public float getTemperature() { return temperature; }

    public void setAmount(int amount) { this.amount = amount; }
    public void setPressure(float pressure) { this.pressure = pressure; }
    public void setTemperature(float temperature) { this.temperature = temperature; }

    public GasStack copy() {
        return new GasStack(gas, amount, pressure, temperature);
    }
}