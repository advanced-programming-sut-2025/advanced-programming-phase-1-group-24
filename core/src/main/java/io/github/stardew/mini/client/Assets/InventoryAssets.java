package io.github.stardew.mini.client.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class InventoryAssets {
    private static InventoryAssets instance;

    public static InventoryAssets getInstance() {
        if (instance == null) instance = new InventoryAssets();
        return instance;
    }

    public static Animation<TextureRegion> toolUsageAnimation;

    public static final Map<Integer, String> DIRECTION_NAMES = Map.of(
        0, "up",
        1, "right",
        2, "down",
        3, "left"
    );

    public static Texture slot;
    public static Texture highlightedSlot;
    public static Texture inventoryMenuBackground;
    public static Texture InitialHOE;
    public static Texture CopperHOE;
    public static Texture IronHOE;
    public static Texture GoldHOE;
    public static Texture IridiumHOE;
    public static Texture InitialPICKAXE;
    public static Texture CopperPICKAXE;
    public static Texture IronPICKAXE;
    public static Texture GoldPICKAXE;
    public static Texture IridiumPICKAXE;
    public static Texture InitialAXE;
    public static Texture CopperAXE;
    public static Texture IronAXE;
    public static Texture GoldAXE;
    public static Texture IridiumAXE;
    public static Texture InitialWATERINGCAN;
    public static Texture CopperWATERINGCAN;
    public static Texture IronWATERINGCAN;
    public static Texture GoldWATERINGCAN;
    public static Texture IridiumWATERINGCAN;
    public static Texture InitialFISHINGPOLE;
    public static Texture BambooFISHINGPOLE;
    public static Texture FiberglassFISHINGPOLE;
    public static Texture IridiumFISHINGPOLE;
    public static Texture InitialSCYTHE;
    public static Texture InitialMILKPAIL;
    public static Texture InitialSHEAR;
    public static Texture InitialTRASHCAN;
    public static Texture CopperTRASHCAN;
    public static Texture IronTRASHCAN;
    public static Texture GoldTRASHCAN;
    public static Texture IridiumTRASHCAN;
    public static Texture maxEnergyBuff;
    public static Texture fishingBuff;
    public static Texture foragingBuff;
    public static Texture farmingBuff;
    public static Texture miningBuff;

    public static final Map<String, Texture> toolMap = new HashMap<>();

    public static void load() {
        slot = new Texture(Gdx.files.internal("NewInventory/slot.jpg"));
        highlightedSlot = new Texture(Gdx.files.internal("NewInventory/highlight.png"));
        inventoryMenuBackground = new Texture(Gdx.files.internal("NewInventory/inventoryMenu.png"));
        InitialHOE = new Texture(Gdx.files.internal("Hoe/Hoe.png"));
        CopperHOE = new Texture(Gdx.files.internal("Hoe/Copper_Hoe.png"));
        IronHOE = new Texture(Gdx.files.internal("Hoe/Steel_Hoe.png"));
        GoldHOE = new Texture(Gdx.files.internal("Hoe/Gold_Hoe.png"));
        IridiumHOE = new Texture(Gdx.files.internal("Hoe/Iridium_Hoe.png"));
        InitialPICKAXE = new Texture(Gdx.files.internal("Tools/Pickaxe/Pickaxe.png"));
        CopperPICKAXE = new Texture(Gdx.files.internal("Tools/Pickaxe/Copper_Pickaxe.png"));
        IronPICKAXE = new Texture(Gdx.files.internal("Tools/Pickaxe/Steel_Pickaxe.png"));
        GoldPICKAXE = new Texture(Gdx.files.internal("Tools/Pickaxe/Gold_Pickaxe.png"));
        IridiumPICKAXE = new Texture(Gdx.files.internal("Tools/Pickaxe/Iridium_Pickaxe.png"));
        InitialAXE = new Texture(Gdx.files.internal("Tools/Axe/Axe.png"));
        CopperAXE = new Texture(Gdx.files.internal("Tools/Axe/Copper_Axe.png"));
        IronAXE = new Texture(Gdx.files.internal("Tools/Axe/Steel_Axe.png"));
        GoldAXE = new Texture(Gdx.files.internal("Tools/Axe/Gold_Axe.png"));
        IridiumAXE = new Texture(Gdx.files.internal("Tools/Axe/Iridium_Axe.png"));
        InitialSCYTHE = new Texture(Gdx.files.internal("Tools/Scythe.png"));
        InitialFISHINGPOLE = new Texture(Gdx.files.internal("Fishing_Pole/Training_Rod.png"));
        BambooFISHINGPOLE = new Texture(Gdx.files.internal("Fishing_Pole/Bamboo_Pole.png"));
        FiberglassFISHINGPOLE = new Texture(Gdx.files.internal("Fishing_Pole/Fiberglass_Rod.png"));
        IridiumFISHINGPOLE = new Texture(Gdx.files.internal("Fishing_Pole/Iridium_Rod.png"));
        InitialMILKPAIL = new Texture(Gdx.files.internal("Tools/Milk_Pail.png"));
        InitialSHEAR = new Texture(Gdx.files.internal("Tools/Shears.png"));
        InitialWATERINGCAN = new Texture(Gdx.files.internal("Watering_Can/Watering_Can.png"));
        CopperWATERINGCAN = new Texture(Gdx.files.internal("Watering_Can/Copper_Watering_Can.png"));
        IronWATERINGCAN = new Texture(Gdx.files.internal("Watering_Can/Steel_Watering_Can.png"));
        GoldWATERINGCAN = new Texture(Gdx.files.internal("Watering_Can/Gold_Watering_Can.png"));
        IridiumWATERINGCAN = new Texture(Gdx.files.internal("Watering_Can/Iridium_Watering_Can.png"));
        InitialTRASHCAN = new Texture(Gdx.files.internal("Tools/Trash_Can_Steel.png"));
        CopperTRASHCAN = new Texture(Gdx.files.internal("Tools/Trash_Can_Copper.png"));
        IronTRASHCAN = new Texture(Gdx.files.internal("Tools/Trash_Can_Steel.png"));
        GoldTRASHCAN = new Texture(Gdx.files.internal("Tools/Trash_Can_Gold.png"));
        IridiumTRASHCAN = new Texture(Gdx.files.internal("Tools/Trash_Can_Iridium.png"));
        maxEnergyBuff = new Texture(Gdx.files.internal("Buff/Max_Energy_Buff.png"));
        fishingBuff = new Texture(Gdx.files.internal("Buff/Fishing_Skill_Icon.png"));
        farmingBuff = new Texture(Gdx.files.internal("Buff/Farming_Skill_Icon.png"));
        foragingBuff = new Texture(Gdx.files.internal("Buff/Foraging_Skill_Icon.png"));
        miningBuff = new Texture(Gdx.files.internal("Buff/Mining_Skill_Icon.png"));
        toolMap.put("INITIALHOE", InitialHOE);
        toolMap.put("COPPERHOE", CopperHOE);
        toolMap.put("IRONHOE", IronHOE);
        toolMap.put("GOLDHOE", GoldHOE);
        toolMap.put("IRIDIUMHOE", IridiumHOE);
        toolMap.put("INITIALPICKAXE", InitialPICKAXE);
        toolMap.put("COPPERPICKAXE", CopperPICKAXE);
        toolMap.put("IRONPICKAXE", IronPICKAXE);
        toolMap.put("GOLDPICKAXE", GoldPICKAXE);
        toolMap.put("IRIDIUMPICKAXE", IridiumPICKAXE);
        toolMap.put("INITIALAXE", InitialAXE);
        toolMap.put("COPPERAXE", CopperAXE);
        toolMap.put("IRONAXE", IronAXE);
        toolMap.put("GOLDAXE", GoldAXE);
        toolMap.put("IRIDIUMAXE", IridiumAXE);
        toolMap.put("INITIALWATERINGCAN", InitialWATERINGCAN);
        toolMap.put("COPPERWATERINGCAN", CopperWATERINGCAN);
        toolMap.put("IRONWATERINGCAN", IronWATERINGCAN);
        toolMap.put("GOLDWATERINGCAN", GoldWATERINGCAN);
        toolMap.put("IRIDIUMWATERINGCAN", IridiumWATERINGCAN);
        toolMap.put("TRAININGFISHINGPOLE", InitialFISHINGPOLE);
        toolMap.put("BAMBOOFISHINGPOLE", BambooFISHINGPOLE);
        toolMap.put("FIBERGLASSFISHINGPOLE", FiberglassFISHINGPOLE);
        toolMap.put("IRIDIUMFISHINGPOLE", IridiumFISHINGPOLE);
        toolMap.put("INITIALSCYTHE", InitialSCYTHE);
        toolMap.put("INITIALMILKPAIL", InitialMILKPAIL);
        toolMap.put("INITIALTRASHCAN", InitialTRASHCAN);
        toolMap.put("COPPERTRASHCAN", CopperTRASHCAN);
        toolMap.put("IRONTRASHCAN", IronTRASHCAN);
        toolMap.put("GOLDTRASHCAN", GoldTRASHCAN);
        toolMap.put("IRIDIUMTRASHCAN", IridiumTRASHCAN);
        toolMap.put("INITIALSHEAR", InitialSHEAR);

        Texture toolUsage1 = new Texture(Gdx.files.internal("Tools/use/1.png"));
        Texture toolUsage2 = new Texture(Gdx.files.internal("Tools/use/2.png"));
        Texture toolUsage3 = new Texture(Gdx.files.internal("Tools/use/3.png"));
        toolUsageAnimation = new Animation<>(0.1f, new TextureRegion(toolUsage1), new TextureRegion(toolUsage2), new TextureRegion(toolUsage3));
        toolUsageAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public static void dispose() {
        if (slot != null) slot.dispose();
        if (highlightedSlot != null) highlightedSlot.dispose();
        if (inventoryMenuBackground != null) inventoryMenuBackground.dispose();
        if (miningBuff != null) miningBuff.dispose();
        if (farmingBuff != null) farmingBuff.dispose();
        if (foragingBuff != null) foragingBuff.dispose();
        if (fishingBuff != null) fishingBuff.dispose();
        if (maxEnergyBuff != null) maxEnergyBuff.dispose();
        disposeToolTextures();
    }

    public static void disposeToolTextures() {
        for (Texture texture : toolMap.values()) {
            texture.dispose();
        }
        if (toolMap != null) {
            toolMap.clear();
        }

        for (TextureRegion region : toolUsageAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }

    public static Texture getToolTexture(String textureKey) {
        return toolMap.get(textureKey);
    }

}
