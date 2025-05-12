package org.example.Model.Things;

import org.example.Model.Result;
import org.example.Model.App;
import org.example.Model.Tools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Backpack {
    private StorageType type;
    private int maxSize;
    private Map<Item, Integer> inventoryItems;
    private ArrayList<Tool> tools;
    private TrashCan trashcan;

    public StorageType getType() {
        return type;
    }

    public Map<Item, Integer> getInventoryItems() {
        return inventoryItems;
    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public Backpack() {
        this.type = StorageType.INITIAL;
        this.maxSize = type.getBackpackCapacity();
        this.inventoryItems = new HashMap<Item, Integer>();
        this.tools = new ArrayList<>();
        this.trashcan = new TrashCan(ToolType.TRASHCAN);
        Hoe hoe = new Hoe(ToolType.HOE);
        tools.add(hoe);
        PickAxe pickaxe = new PickAxe(ToolType.PICKAXE);
        tools.add(pickaxe);
        Axe axe = new Axe(ToolType.AXE);
        tools.add(axe);
        WateringCan wateringcan = new WateringCan(ToolType.WATERINGCAN);
        tools.add(wateringcan);
        Scythe scythe = new Scythe(ToolType.SCYTHE);
        tools.add(scythe);
    }

    public void upgrade(StorageType type) {
        this.type = type;
        this.maxSize = type.getBackpackCapacity();
    }

    public Result addItem(Item item, int amount) {
        if (inventoryItems.size() >= maxSize && !inventoryItems.containsKey(item)) {
            return new Result(false, "inventory is full");
        } else if (inventoryItems.containsKey(item)) {
            inventoryItems.put(item, inventoryItems.get(item) + amount);
        } else {
            inventoryItems.put(item, amount);
        }
        return new Result(true, "item added");
    }

    public Result removeItem(String itemName, int amount, boolean withTrashCan) {
        for (Item item : inventoryItems.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                int newAmount = inventoryItems.get(item) - amount;
                if (newAmount <= 0) {
                    if (withTrashCan) {
                        trashcan.useTrashCan(item, inventoryItems.get(item));
                    }
                    inventoryItems.remove(item);
                } else {
                    if (withTrashCan) {
                        trashcan.useTrashCan(item, amount);
                    }
                    inventoryItems.put(item, newAmount);
                }
                return new Result(true, "item removed successfully");
            }
        }
        return new Result(false, "item not found");
    }

    public Result grabItem(String itemName, int amount) {
        for (Item item : inventoryItems.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                int newAmount = inventoryItems.get(item) - amount;
                if (newAmount == 0) {
                    inventoryItems.remove(item);
                } else if (newAmount < 0) {
                    return new Result(false, "Not enough Items.");
                } else {
                    inventoryItems.put(item, newAmount);
                }
                return new Result(true, "item grabbed successfully");
            }
        }
        return new Result(false, "item not found");
    }

    public Result equipTool(String newToolName) {
        for (Tool existingTool : tools) {
            if (existingTool.getName().equalsIgnoreCase(newToolName)) {
                App.getInstance().getCurrentGame().getCurrentPlayer().setEquippedTool(existingTool);
                return new Result(true, "equipped tool successfully");
            }
        }
        return new Result(false, "tool not found");
    }

    public Result showInventory() {
        StringBuilder output = new StringBuilder();
        for (Item item : inventoryItems.keySet()) {
            output.append(item.toString() + ": " + inventoryItems.get(item) + "\n");
        }
        return new Result(true, output.toString());
    }

    public Result showTools() {
        StringBuilder output = new StringBuilder();
        for (Tool tool : tools) {
            output.append(tool.toString() + "\n");
        }
        return new Result(true, output.toString());
    }

    public Boolean hasTool(String toolName) {
        for (Tool tool : tools) {
            if (tool.getName().equalsIgnoreCase(toolName)) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasItem(String itemName, int amount) {
        for (Item item : inventoryItems.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (inventoryItems.get(item) >= amount) return true;
            }
        }
        return false;
    }

    //    public Backpack copy() {
//        Backpack copy = new Backpack();
//        copy.type = this.type;
//        copy.maxSize = this.maxSize;
//
//        // Deep copy of inventoryItems
//        copy.inventoryItems = new HashMap<>();
//        for (Map.Entry<Item, Integer> entry : this.inventoryItems.entrySet()) {
//            copy.inventoryItems.put(entry.getKey().copy(), entry.getValue());
//        }
//
//        // Deep copy of tools
//        copy.tools = new ArrayList<>();
//        for (Tool tool : this.tools) {
//            copy.tools.add(tool.copy());
//        }
//
//        // Deep copy of trashcan
//        copy.trashcan = this.trashcan.copy();
//
//        return copy;
//    }

    public Item grabItemAndReturn(String itemName, int amount) {
        for (Item item : inventoryItems.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                int currentAmount = inventoryItems.get(item);
                int newAmount = currentAmount - amount;
                if (newAmount < 0) {
                    return null; // Not enough quantity
                }
                Item itemCopy = item.copy();
                if (newAmount == 0) {
                    inventoryItems.remove(item);
                } else {
                    inventoryItems.put(item, newAmount);
                }
                return itemCopy;
            }
        }
        return null; // Item not found
    }


    public int getItemCount(String itemName) {
        for (Item item : inventoryItems.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return inventoryItems.get(item);
            }
        }
        return 0;
    }

    public TrashCan getTrashcan() {
        return trashcan;
    }

    public Tool getTool(String toolName) {
        for (Tool tool : tools) {
            if (tool.getName().equalsIgnoreCase(toolName)) {
                return tool;
            }
        }
        return null;
    }
}
