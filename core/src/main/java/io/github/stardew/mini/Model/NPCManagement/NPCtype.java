package io.github.stardew.mini.Model.NPCManagement;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Point;

public enum NPCtype {
    Sebastian("Sebastian", Arrays.asList("Wool", "PumpkinPie", "Pizza"), Arrays.asList("Coffee", "Squid"),
        new Point(72, 55),
        new Point(72, 55),
        new Point(95, 85)),
    Abigail("Abigail",Arrays.asList("Rock", "Iron", "Coffee"),Arrays.asList("Spaghetti","Tilapia"),
        new Point(72, 65),
        new Point(53, 77),
        new Point(96, 85)),
    Harvey("Harvey",Arrays.asList("Coffee", "Pickle", "Wine"),Arrays.asList("Mystic Syrup","Lion Fish"),
        new Point(72, 75),
        new Point(94, 85),
        new Point(94, 85)),
    Leah("Leah",Arrays.asList("Salad", "Grapes", "Wine"), Arrays.asList("Pomegranate", "Apricot"),
        new Point(72, 85),
        new Point(94, 58),
        new Point(93, 85)),
    Robin("Robin",Arrays.asList("Spaghetti", "Wood", "Iron Bar"), Arrays.asList("Cookie", "Pancakes"),
        new Point(72, 95),
        new Point(53, 87),
        new Point(92, 85));

    private final String name;
    private final ArrayList<Dialog> dialogs;
    private final List<String> favoriteItems;
    private final List<String> randomGifts;
    private TextureRegion textureRegion;
    private final Point homeLocation;
    private final Point workLocation;
    private final Point socialLocation;

    NPCtype(String name, List<String> favoriteItems, List<String> randomGifts,
            Point homeLocation, Point workLocation, Point socialLocation
    ) {
        this.name = name;
        this.dialogs = Dialog.getDialogs(name);
        this.favoriteItems = favoriteItems;
        this.randomGifts = randomGifts;
        this.homeLocation = homeLocation;
        this.workLocation = workLocation;
        this.socialLocation = socialLocation;
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
    public Point getHomeLocation() {
        return homeLocation;
    }

    public Point getWorkLocation() {
        return workLocation;
    }

    public Point getSocialLocation() {
        return socialLocation;
    }
}
