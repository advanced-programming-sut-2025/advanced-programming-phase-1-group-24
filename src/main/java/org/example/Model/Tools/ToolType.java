package org.example.Model.Tools;

import org.example.Model.Skill;

public enum ToolType {
    HOE("hoe"),
    PICKAXE("pick axe"),
    AXE("axe"),
    WATERINGCAN("watering can"),
    FISHINGPOLE("fishing pole"),
    SCYTHE("scythe"),
    MILKPAIL("milkPail"),
    SHEAR("shear"),
    TRASHCAN("trash can"),
    ;
    private final String name;

    //    private final int price;
//    private final Skill relatedSkill;
    ToolType(String name) {
        this.name = name;
    }

    public void breakTool() {
    }

    public String getName() {
        return name;
    }

    public void useTool() {
    } //includes using fishingpole.
    public static ToolType fromString(String toolString) {
            for (ToolType tool : ToolType.values()) {
                if (toolString.equalsIgnoreCase(tool.name)) {
                    return tool;
                }
            }
        return null;
    }
}
