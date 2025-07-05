package io.github.stardew.mini.Model.Assets;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CropAssets {
    private static CropAssets instance;

    public static CropAssets getInstance() {
        if (instance == null) instance = new CropAssets();
        return instance;
    }

    // Map of crop name to list of textures (1 per stage + final)
    public static final Map<String, List<Texture>> cropTextures = new HashMap<>();

    public static void load() {
        addCrop("Ancient_Fruit", 5);
        addCrop("Amaranth", 4);
        addCrop("Artichoke", 5);
        addCrop("Beet", 4);
        addCrop("Blue_Jazz", 4); // List.of(1, 2, 2, 2) => 4 stages → 5 images
        addCrop("Blueberry", 5);
        addCrop("Bok_Choy", 4);
        addCrop("Broccoli", 4);
        addCrop("Cauliflower", 5);
        addCrop("Carrot", 3);
        addCrop("Coffee_Bean", 5);
        addCrop("Corn", 5);
        addCrop("Cranberries", 5);
        addCrop("Eggplant", 4);
        addCrop("Fairy_Rose", 4);
        addCrop("Garlic", 4);
        addCrop("Grapes", 5);
        addCrop("Green_Bean", 5);
        addCrop("HotPepper", 5);
        addCrop("Hops", 5);
        addCrop("Kale", 4);
        addCrop("Melon", 5);
        addCrop("Parsnip", 4);
        addCrop("Poppy", 4);
        addCrop("Potato", 5);
        addCrop("Powdermelon", 5);
        addCrop("Pumpkin", 5);
        addCrop("Radish", 4);
        addCrop("Red_Cabbage", 5);
        addCrop("Rhubarb", 5);
        addCrop("Starfruit", 5);
        addCrop("Strawberry", 5);
        addCrop("Summer_Spangle", 4);
        addCrop("Summer_Squash", 5);
        addCrop("Sunflower", 4);
        addCrop("Sweet_Gem_Berry", 5);
        addCrop("Tomato", 5);
        addCrop("Tulip", 4);
        addCrop("UnmilledRice", 4);
        addCrop("Wheat", 4);
        addCrop("Yam", 4);
    }

    private static void addCrop(String cropName, int growthStages) {
        List<Texture> textures = new ArrayList<>();
        for (int i = 1; i <= growthStages + 1; i++) {
            String path = "Crops/" + cropName + "_Stage_" + i + ".png";
            textures.add(new Texture(path));
        }
        cropTextures.put(cropName, textures);
    }

    public static void dispose() {
        for (List<Texture> textures : cropTextures.values()) {
            for (Texture tex : textures) {
                tex.dispose();
            }
        }
        cropTextures.clear();
    }
}
