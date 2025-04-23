package org.example.Model.Things;


import org.example.Model.Tools.Tool;

import java.util.Map;

public class Backpack {
    BackpackType type;

    public BackpackType getType() {
        return type;
    }

    Map<Item, Integer> inventoryItems;
    Map<Tool, Integer> tools;

}
