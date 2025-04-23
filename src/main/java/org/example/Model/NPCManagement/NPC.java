package org.example.Model.NPCManagement;

import Model.MapManagement.Tile;
import Model.Result;
import Model.Things.Item;

import java.util.ArrayList;
import java.util.Map;

public class NPC{
    NPCnames npcName;
    ArrayList<Dialog> dialogs;
    Tile currentTile;

    Map<ArrayList<Item>, ArrayList<Item>> missions;
    //requiredItems, prizeItems

    public Result talkToNPC(){}
    public void doMission(){}
    public void giveGift(){}


}
