package org.example.Model.Things;

import Model.Tools.Tool;

import java.util.Map;

public class Backpack {
    BackpackType type;

    public BackpackType getType() {
        return type;
    }

    Map<Item, Integer> inventoryItems;
    //Map<Item, Integer> 
    Map<Tool, Integer> tools;
    //ArrayList seed
    

}
