package org.example.Model.Reccepies;

import Model.Things.Item;

import java.util.ArrayList;

public enum Machine {
    ;

    public void useMachine(){} //overridden later

    private final ArrayList<Item> recipe;

    Machine(ArrayList<Item> recipe) {
        this.recipe = recipe;
    }

    public ArrayList<Item> getRecipe() {
        return recipe;
    }
}
