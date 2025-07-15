package io.github.stardew.mini.Model.NPCManagement;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum NPCtype {
    Sebastian("Sebastian", Arrays.asList("Wool", "PumpkinPie", "Pizza"), Arrays.asList("Coffee", "Squid")),
    Abigail("Abigail",Arrays.asList("Rock", "Iron", "Coffee"),Arrays.asList("Spaghetti","Tilapia")),
    Harvey("Harvey",Arrays.asList("Coffee", "Pickle", "Wine"),Arrays.asList("Mystic Syrup","Lion Fish")),
    Leah("Leah",Arrays.asList("Salad", "Grapes", "Wine"), Arrays.asList("Pomegranate", "Apricot")),
    Robin("Robin",Arrays.asList("Spaghetti", "Wood", "Iron Bar"), Arrays.asList("Cookie", "Pancakes"));

    private final String name;
    private final ArrayList<Dialog> dialogs;
    private final List<String> favoriteItems;
    private final List<String> randomGifts;
    private TextureRegion textureRegion;

    NPCtype(String name, List<String> favoriteItems, List<String> randomGifts) {
        this.name = name;
        this.dialogs = Dialog.getDialogs(name);
        this.favoriteItems = favoriteItems;
        this.randomGifts = randomGifts;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Dialog> getDialogs() {
        return dialogs;
    }

    public List<String> getFavoriteItems() {
        return favoriteItems;
    }

    public List<String> getRandomGifts() {return randomGifts;}

    public TextureRegion getTextureRegion() {return textureRegion;}

    public void initTexture() {
        Texture fullTexture = new Texture("game/character/" + name + ".png");
        TextureRegion[][] frames = TextureRegion.split(fullTexture, 16, 32);
        this.textureRegion = frames[0][0];
    }
}
