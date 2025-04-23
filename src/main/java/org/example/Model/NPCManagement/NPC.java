package org.example.Model.NPCManagement;


import org.example.Model.MapManagement.Tile;
import org.example.Model.Result;
import org.example.Model.Things.Item;

import java.util.ArrayList;
import java.util.Map;

public class NPC{
    NPCnames npcName;
    ArrayList<Dialog> dialogs;
    Tile currentTile;

    Map<ArrayList<Item>, ArrayList<Item>> missions;
    //requiredItems, prizeItems

    //public Result talkToNPC(){}
    public void doMission(){}
    public void giveGift(){}


}
