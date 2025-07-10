package io.github.stardew.mini.Model.Tools;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.stardew.mini.Model.Things.ToolMaterial;
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "toolType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Hoe.class, name = "Hoe"),
    @JsonSubTypes.Type(value = WateringCan.class, name = "WateringCan"),
    @JsonSubTypes.Type(value = TrashCan.class, name = "TrashCan"),
    @JsonSubTypes.Type(value = Scythe.class, name = "Scythe"),
    @JsonSubTypes.Type(value = PickAxe.class, name = "PickAxe"),
    @JsonSubTypes.Type(value = Axe.class, name = "Axe"),
    @JsonSubTypes.Type(value = Shear.class, name = "Shear"),
    @JsonSubTypes.Type(value = MilkPail.class, name = "MilkPail"),
    @JsonSubTypes.Type(value = FishingPole.class, name = "FishingPole"),
    // Add others...
})
public  class Tool {
    String name;
    ToolType type;
    ToolMaterial material;

    public Tool(ToolType type) {
        this.name =type.getName();
        this.type = type;
        this.material = ToolMaterial.Initial;
    }

    public Tool() {
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
