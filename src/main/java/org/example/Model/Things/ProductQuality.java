package org.example.Model.Things;

public enum ProductQuality {
    Normal(1,0,0.5),
    Silver(1.25,0.5,0.7),
    Golden(1.5,0.7,0.9),
    Iridium(2,0.9,1);

    private double priceCoefficient;
    private double obtainedAmountStart;
    private double obtainedAmountEnd;

    ProductQuality(double priceCoefficient, double obtainedAmountStart, double obtainedAmountEnd) {
        this.priceCoefficient = priceCoefficient;
        this.obtainedAmountStart = obtainedAmountStart;
        this.obtainedAmountEnd = obtainedAmountEnd;
    }
    public static ProductQuality getQualityByValue(double value) {
        for (ProductQuality quality : ProductQuality.values()) {
            if (value >= quality.obtainedAmountStart && value < quality.obtainedAmountEnd) {
                return quality;
            }
        }
        // If value is exactly 1.0 or no match (should only happen with value == 1), return the highest quality
        return ProductQuality.Iridium;
    }

    public double getPriceCoefficient() {
        return priceCoefficient;
    }
}
