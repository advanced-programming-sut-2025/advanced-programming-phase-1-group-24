package org.example.Model.Tools;

import org.example.Model.Skill;

public enum ToolType {
    HOE(),
    PICKAXE,
    AXE,
    WATERINGCAN,
    FISHINGPOLE,
    SCYTHE,
    MILKPAIL,
    SHEAR,
    TRASHCAN;


    int price;
    Skill relatedSkill;

    public void breakTool(){}
    public void useTool(){} //includes using fishingpole.

}
