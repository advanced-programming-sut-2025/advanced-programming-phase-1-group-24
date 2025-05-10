package org.example.Model.Things;

public enum ForagingMineralType {
    Quartz("Quartz", "A clear crystal commonly found in caves and mines.", 25),
    Earth_Crystal("Earth Crystal", "A resinous substance found near the surface.", 50),
    Frozen_Tear("Frozen Tear", "A crystal fabled to be the frozen tears of a yeti.", 75),
    Fire_Quartz("Fire Quartz", "A glowing red crystal commonly found near hot lava.", 100),
    Emerald("Emerald", "A precious stone with a brilliant green color.", 250),
    Aquamarine("Aquamarine", "A shimmery blue-green gem.", 180),
    Ruby("Ruby", "A precious stone that is sought after for its rich color and beautiful luster.", 250),
    Amethyst("Amethyst", "A purple variant of quartz.", 100),
    Topaz("Topaz", "Fairly common but still prized for its beauty.", 80),
    Jade("Jade", "A pale green ornamental stone.", 200),
    Diamond("Diamond", "A rare and valuable gem.", 750),
    Prismatic_Shard("Prismatic Shard", "A very rare and powerful substance with unknown origins.", 2000),
    Copper("Copper", "A common ore that can be smelted into bars.", 5),
    Iron("Iron", "A fairly common ore that can be smelted into bars.", 10),
    Gold("Gold", "A precious ore that can be smelted into bars.", 25),
    Iriduim("Iridium", "An exotic ore with many curious properties. Can be smelted into bars.", 100),
    Coal("Coal", "A combustible rock that is useful for crafting and smelting.", 15);


    private final String name;
    private final String description;
    private final int sellPrice;

    ForagingMineralType(String name, String description, int sellPrice) {
        this.name = name;
        this.description = description;
        this.sellPrice = sellPrice;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
