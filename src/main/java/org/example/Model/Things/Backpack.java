package org.example.Model.Things;

import org.example.Model.Tools.Tool;

import java.util.Map;

public class Backpack {
    StorageType type;

    public StorageType getType() {
        return type;
    }

    Map<Item, Integer> inventoryItems;
    //Map<Item, Integer>
    Map<Tool, Integer> tools;
    //ArrayList seed


}
