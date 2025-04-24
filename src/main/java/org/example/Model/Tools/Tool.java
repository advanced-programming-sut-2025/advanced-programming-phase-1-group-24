package org.example.Model.Tools;

import org.example.Model.Things.Item;
import org.example.Model.Things.Material;

public class Tool extends Item {
    ToolType type;
    int level;
    Material material;

    public ToolType getType() {
        return type;
    }
}
