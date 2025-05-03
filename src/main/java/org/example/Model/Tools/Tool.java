package org.example.Model.Tools;

import org.example.Model.Things.ToolMaterial;

public abstract class Tool {
    String name;
    ToolType type;
    ToolMaterial material;

    public Tool(ToolType type) {
        this.name =type.name();
        this.type = type;
        this.material = ToolMaterial.Initial;
    }

    public ToolType getType() {
        return type;
    }
    public String getName() { return name; }

    public String toString() {
        return name;
    }

    public void upgrade(ToolMaterial material) {
        this.material = material;
    }
}
