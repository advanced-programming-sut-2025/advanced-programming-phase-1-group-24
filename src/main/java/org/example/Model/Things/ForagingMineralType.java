package org.example.Model.Things;

public enum ForagingMineralType {
    Quartz("A clear crystal commonly found in caves and mines.", 25),
    Earth_Crystal("A resinous substance found near the surface.", 50),
    Frozen_Tear("A crystal fabled to be the frozen tears of a yeti.", 75),
    Fire_Quartz("A glowing red crystal commonly found near hot lava.", 100),
    Emerald("A precious stone with a brilliant green color.", 250),
    Aquamarine("A shimmery blue-green gem.", 180),
    Ruby("A precious stone that is sought after for its rich color and beautiful luster.", 250),
    Amethyst("A purple variant of quartz.", 100),
    Topaz("Fairly common but still prized for its beauty.", 80),
    Jade("A pale green ornamental stone.", 200),
    Diamond("A rare and valuable gem.", 750),
    Prismatic_Shard("A very rare and powerful substance with unknown origins.", 2000),
    Copper("A common ore that can be smelted into bars.", 5),
    Iron("A fairly common ore that can be smelted into bars.", 10),
    Gold("A precious ore that can be smelted into bars.", 25),
    Iriduim("An exotic ore with many curious properties. Can be smelted into bars.", 100),
    Coal("A combustible rock that is useful for crafting and smelting.", 15);

    private final String description;
    private final int sellPrice;

    ForagingMineralType(String description, int sellPrice) {
        this.description = description;
        this.sellPrice = sellPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
