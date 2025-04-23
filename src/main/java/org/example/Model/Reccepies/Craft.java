package org.example.Model.Reccepies;


import org.example.Model.Things.Item;

import java.util.ArrayList;

public enum Craft {
    ;
    private final ArrayList<Item> recipe;

    Craft(ArrayList<Item> recipe) {
        this.recipe = recipe;
    }

    public ArrayList<Item> getRecipe() {
        return recipe;
    }
}
