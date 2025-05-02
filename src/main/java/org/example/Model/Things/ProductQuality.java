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
}

