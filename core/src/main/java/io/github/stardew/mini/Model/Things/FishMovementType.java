package io.github.stardew.mini.Model.Things;

/**
 * Defines the distinct movement patterns for fish in the fishing minigame.
 */
public enum FishMovementType {
    MIXED(0.8f, 0.7f, 0.8f),    // Slower base speed, less acceleration, good predictability
    SMOOTH(0.3f, 0.5f, 0.98f),  // Very slow, very smooth, highly predictable
    SINKER(0.7f, 0.6f, 0.9f),   // Slower than mixed, slightly more predictable, with downward bias
    FLOATER(0.7f, 0.6f, 0.9f),  // Slower than mixed, slightly more predictable, with upward bias
    DART(1.5f, 1.2f, 0.6f);     // Faster, more erratic

    private final float baseRandomAccelerationFactor;
    private final float baseMaxSpeedFactor;
    private final float predictabilityFactor;

    FishMovementType(float baseRandomAccelerationFactor, float baseMaxSpeedFactor, float predictabilityFactor) {
        this.baseRandomAccelerationFactor = baseRandomAccelerationFactor;
        this.baseMaxSpeedFactor = baseMaxSpeedFactor;
        this.predictabilityFactor = predictabilityFactor;
    }

    public float getBaseRandomAccelerationFactor() {
        return baseRandomAccelerationFactor;
    }

    public float getBaseMaxSpeedFactor() {
        return baseMaxSpeedFactor;
    }

    public float getPredictabilityFactor() {
        return predictabilityFactor;
    }
}
