package org.example.Model.Reccepies;

import org.example.Model.Things.Item;
import org.example.Model.Things.ProductQuality;

import java.util.Objects;

public class randomStuff extends Item {
    randomStuffType type;

    public randomStuff(int price, randomStuffType type) {
        super(type.getName(), true, price, false, ProductQuality.Normal,true);
        this.type = type;
    }
    @Override
    public randomStuff copy() {
        randomStuff newStuff = new randomStuff( getPrice(), type);
        newStuff.setSellable(isSellable());
        newStuff.setPlaceable(isPlaceable());
        newStuff.setQuality(getProductQuality());
        newStuff.setEatable(true); // or conditionally based on type
        return newStuff;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof randomStuff that)) return false;
        if (!super.equals(o)) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }


}
