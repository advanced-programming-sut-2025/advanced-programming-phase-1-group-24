package org.example.Model.Reccepies;

import Model.Things.Item;

import java.util.ArrayList;

public enum Food {
    ;

    private final ArrayList<Item> recipe;

    Food(ArrayList<Item> recipe) {
        this.recipe = recipe;
    }

    public ArrayList<Item> getRecipe() {
        return recipe;
    }
}