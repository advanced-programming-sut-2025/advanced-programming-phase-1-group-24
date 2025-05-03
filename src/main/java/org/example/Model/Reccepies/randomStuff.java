package org.example.Model.Reccepies;

import org.example.Model.Things.Item;
import org.example.Model.Things.ProductQuality;

public class randomStuff extends Item {
    randomStuffType type;

    public randomStuff(String name, boolean isSellable, int price, boolean isPlaceable, ProductQuality productQuality, randomStuffType type) {
        super(name, isSellable, price, isPlaceable, productQuality);
        this.type = type;
    }
}
