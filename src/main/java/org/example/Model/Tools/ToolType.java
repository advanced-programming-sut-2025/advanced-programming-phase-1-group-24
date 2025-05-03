package org.example.Model.Tools;


import org.example.Model.Skill;

public enum ToolType {
    HOE(),
    PICKAXE(),
    AXE(),
    WATERINGCAN(),
    FISHINGPOLE(),
    SCYTHE(),
    MILKPAIL(),
    SHEAR(),
    TRASHCAN();

//    private final int price;
//    private final Skill relatedSkill;

    public void breakTool(){}
    public void useTool(){} //includes using fishingpole.

}
