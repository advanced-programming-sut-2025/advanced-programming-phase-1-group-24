package io.github.stardew.mini.model.item;

// TileDescriptionId.java
public enum TileDescriptionId {
    SOIL("game/tiles/soil.png"),
    GRASS("game/tiles/grass.png"),
    WATER("game/tiles/water.png"),
    SLOT("game/tiles/slot.png"),
    HIGHLIGHT("game/tiles/highlight.png"),
    ;
    final String iconPath;

    TileDescriptionId(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }
}
