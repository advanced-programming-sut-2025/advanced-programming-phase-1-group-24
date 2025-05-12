package org.example.Model.Places;

import java.util.ArrayList;

import org.example.Model.Reccepies.Machine;
import org.example.Model.Reccepies.MachineType;

public class House extends Place{
  ArrayList<Machine> machines;

  public House(int startX, int startY, int width, int height) {
    this.x = startX;
    this.y = startY;
    this.width = width;
    this.height = height;
    machines = new ArrayList<>();
  }

  public ArrayList<Machine> getMachines() {
    return machines;
  }
    
}
