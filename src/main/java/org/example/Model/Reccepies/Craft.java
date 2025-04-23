package org.example.Model.Reccepies;

import Model.Things.Item;

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
