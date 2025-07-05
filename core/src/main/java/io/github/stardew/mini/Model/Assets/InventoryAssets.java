package io.github.stardew.mini.Model.Assets;

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
    public static Texture InitialHOE;
    public static Texture CopperHOE;
    public static Texture IronHOE;
    public static Texture GoldHOE;
    public static Texture IridiumHOE;
    public static Texture InitialPICKAXE;
    public static Texture InitialAXE;
    public static Texture InitialWATERINGCAN;
    public static Texture InitialFISHINGPOLE;
    public static Texture InitialSCYTHE;
    public static Texture InitialMILKPAIL;
    public static Texture InitialTRASHCAN;
    public static Texture InitialSHEAR;

    public static final Map<String, Texture> toolMap = new HashMap<>();

    public static void load() {
        slot = new Texture(Gdx.files.internal("game/tiles/slot.png"));
        highlightedSlot = new Texture(Gdx.files.internal("game/tiles/highlight.png"));
        InitialHOE = new Texture(Gdx.files.internal("Hoe/Hoe.png"));
        CopperHOE = new Texture(Gdx.files.internal("Hoe/Copper_Hoe.png"));
        IronHOE = new Texture(Gdx.files.internal("Hoe/Steel_Hoe.png"));
        GoldHOE = new Texture(Gdx.files.internal("Hoe/Gold_Hoe.png"));
        IridiumHOE = new Texture(Gdx.files.internal("Hoe/Iridium_Hoe.png"));
        InitialPICKAXE = new Texture(Gdx.files.internal("Tools/Pickaxe/Pickaxe.png"));
        InitialAXE = new Texture(Gdx.files.internal("Tools/Axe/Axe.png"));
        InitialSCYTHE = new Texture(Gdx.files.internal("Tools/Scythe.png"));
        InitialFISHINGPOLE = new Texture(Gdx.files.internal("Fishing_Pole/Training_Rod.png"));
        InitialMILKPAIL = new Texture(Gdx.files.internal("Tools/Milk_Pail.png"));
        InitialSHEAR = new Texture(Gdx.files.internal("Tools/Shears.png"));
        InitialWATERINGCAN = new Texture(Gdx.files.internal("Watering_Can/Watering_Can.png"));
        InitialTRASHCAN = new Texture(Gdx.files.internal("Tools/Trash_Can_Steel.png"));
        toolMap.put("INITIALHOE", InitialHOE);
        toolMap.put("COPPERHOE", CopperHOE);
        toolMap.put("IRONHOE", IronHOE);
        toolMap.put("GOLDHOE", GoldHOE);
        toolMap.put("IRIDIUMHOE", IridiumHOE);
        toolMap.put("INITIALPICKAXE", InitialPICKAXE);
        toolMap.put("INITIALAXE", InitialAXE);
        toolMap.put("INITIALWATERINGCAN", InitialWATERINGCAN);
        toolMap.put("INITIALFISHINGPOLE", InitialFISHINGPOLE);
        toolMap.put("INITIALSCYTHE", InitialSCYTHE);
        toolMap.put("INITIALMILKPAIL", InitialMILKPAIL);
        toolMap.put("INITIALTRASHCAN", InitialTRASHCAN);
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
        disposeToolTextures();
    }


    public static Texture getToolTexture(String textureKey) {
        return toolMap.get(textureKey);
    }

    public static void disposeToolTextures() {
        if (InitialHOE != null) InitialHOE.dispose();
        if (CopperHOE != null) CopperHOE.dispose();
        if (IronHOE != null) IronHOE.dispose();
        if (GoldHOE != null) GoldHOE.dispose();
        if (IridiumHOE != null) IridiumHOE.dispose();
        if (InitialPICKAXE != null) InitialPICKAXE.dispose();
        if (InitialAXE != null) InitialAXE.dispose();
        if (InitialWATERINGCAN != null) InitialWATERINGCAN.dispose();
        if (InitialFISHINGPOLE != null) InitialFISHINGPOLE.dispose();
        if (InitialSCYTHE != null) InitialSCYTHE.dispose();
        if (InitialMILKPAIL != null) InitialMILKPAIL.dispose();
        if (InitialTRASHCAN != null) InitialTRASHCAN.dispose();
        if (InitialSHEAR != null) InitialSHEAR.dispose();

        if (toolMap != null) {
            toolMap.clear();
        }

        for (TextureRegion region : toolUsageAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }
}
