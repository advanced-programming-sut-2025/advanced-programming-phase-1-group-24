package org.example.Model.Places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.example.Model.Reccepies.Machine;
import org.example.Model.Reccepies.MachineType;
import org.example.Model.Things.Food;

public class House extends Place{
  ArrayList<Machine> machines;
  Map<Food, Integer> fridge;

  public House(int startX, int startY, int width, int height) {
    this.x = startX;
    this.y = startY;
    this.width = width;
    this.height = height;
    machines = new ArrayList<>();
    fridge = new HashMap<>();
  }

  public ArrayList<Machine> getMachines() {
    return machines;
  }

  public Map<Food, Integer> getFridge() {  return fridge; }

  public void addFoodToFridge(Food food) {
    fridge.put(food, fridge.getOrDefault(food, 0) + 1);
  }

  public Food grabFoodFromFridge(String foodName) {
    for (Food food : fridge.keySet()) {
      if (food.getName().equals(foodName)) {
        int count = fridge.get(food);
        if (count > 1) {
          fridge.put(food, count - 1);
        } else {
          fridge.remove(food);
        }
        return food;
      }
    }
    return null;
  }

}
