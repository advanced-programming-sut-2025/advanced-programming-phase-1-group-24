package org.example.Model.Tools;

import Model.Things.Item;
import Model.Things.Material;

public class Tool extends Item {
    ToolType type;
    int level;
    Material material;

    public ToolType getType() {
        return type;
    }
}
