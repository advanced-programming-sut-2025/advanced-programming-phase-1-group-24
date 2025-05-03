package org.example.Model.Things;

public enum ProductQuality {
    Normal(1, 0, 0.5),
    Silver(1.25, 0.5, 0.7),
    Golden(1.5, 0.7, 0.9),
    Iridium(2, 0.9, 1000);

    private final double priceCoefficient;
    private final double obtainedAmountStart;
    private final double obtainedAmountEnd;

    ProductQuality(double priceCoefficient, double obtainedAmountStart, double obtainedAmountEnd) {
        this.priceCoefficient = priceCoefficient;
        this.obtainedAmountStart = obtainedAmountStart;
        this.obtainedAmountEnd = obtainedAmountEnd;
    }

    public double getPriceCoefficient() {
        return priceCoefficient;
    }

    public double getObtainedAmountStart() {
        return obtainedAmountStart;
    }

    public double getObtainedAmountEnd() {
        return obtainedAmountEnd;
    }

    public static ProductQuality findQuality(double obtainedQuality) {
        for (ProductQuality quality : ProductQuality.values()) {
            if (quality.getObtainedAmountEnd() >= obtainedQuality
                    && quality.getObtainedAmountStart() <= obtainedQuality) {
                return quality;
            }
        }
        return null;
    }
}
