package io.github.stardew.mini.Model.MapManagement;

import com.badlogic.gdx.graphics.Texture;
import io.github.stardew.mini.Model.Assets.GameAssetManager;

public enum TileType {
    EMPTY("E", "\u001B[47m"),          // White background
    LAKE("L", "\u001B[44m"),           // Blue
    GREENHOUSE("G", "\u001B[102m"),    // Bright Green
    WATERCONTAINER("W", "\u001B[46m"), // Cyan
    CAGE("C", "\u001B[43m"),           // Yellow
    BARN("B", "\u001B[41m"),           // Red
    QUARRY("Q", "\u001B[45m"),         // Magenta
    HOUSE("H", "\u001B[105m"),         // Bright Magenta
    GRASS("g", "\u001B[42m"),          // Green
    FARM("F", "\u001B[103m"),          // Bright Yellow
    WALL("w", "\u001B[47m"),           // White
    NPCLAND("N", "\u001B[106m"),       // Bright Cyan
    NPCHOUSE("h", "\u001B[105m"),
    DOOR("D", "\u001B[41m"), // White
    SHOP("S", "\u001B[42m"),           // Green
    SHIPPINGBIN("s","\u001B[41m");

    private final String letterToPrint;
    private final String backgroundColor;
    private Texture texture;
    TileType(String letterToPrint, String backgroundColor) {
        this.letterToPrint = letterToPrint;
        this.backgroundColor = backgroundColor;
    }

    public String getLetterToPrint() {
        return letterToPrint;
    }

    public String coloredSymbol() {
        return "\u001B[37m" + backgroundColor + letterToPrint + "\u001B[0m";
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public static void initTextures() {
        EMPTY.setTexture(GameAssetManager.FLOORING_28);
        LAKE.setTexture(GameAssetManager.FLOORING_26);
        GREENHOUSE.setTexture(GameAssetManager.GREENHOUSE);
        WATERCONTAINER.setTexture(GameAssetManager.FLOORING_86);
        CAGE.setTexture(GameAssetManager.FLOORING_03);
        BARN.setTexture(GameAssetManager.DARK_GREEN_FLOOR);
        QUARRY.setTexture(GameAssetManager.FLOORING_55);
        HOUSE.setTexture(GameAssetManager.FLOORING_17);
        GRASS.setTexture(GameAssetManager.LIGHT_GREEN_FLOOR);
        FARM.setTexture(GameAssetManager.FLOORING_29);
        WALL.setTexture(GameAssetManager.FLOORING_52);
        NPCLAND.setTexture(GameAssetManager.FLOORING_09);
        NPCHOUSE.setTexture(GameAssetManager.FLOORING_10);
        DOOR.setTexture(GameAssetManager.FLOORING_71);
        SHOP.setTexture(GameAssetManager.FLOORING_84);
        SHIPPINGBIN.setTexture(GameAssetManager.FLOORING_01);
    }

}
