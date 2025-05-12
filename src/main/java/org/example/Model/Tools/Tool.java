package org.example.Model.Tools;

import org.example.Model.Things.ToolMaterial;

public  class Tool {
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
    @Override
    public String toString() {
        return name;
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    public void upgrade(ToolMaterial material) {
        this.material = material;
    }
    public Tool copy() {
        Tool copy = new Tool(this.type);
        copy.upgrade(this.material);
        return copy;
    }
}
