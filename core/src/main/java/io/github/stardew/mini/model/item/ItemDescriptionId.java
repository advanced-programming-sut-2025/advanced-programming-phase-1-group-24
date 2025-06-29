package io.github.stardew.mini.model.item;

import com.badlogic.gdx.utils.Null;
import io.github.stardew.mini.model.game.GameModel;
import kotlin.jvm.functions.Function2;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

// ItemDescriptionId.java
public enum ItemDescriptionId {
    CARROT(0, "carrot", 100, "game/crops/Carrot.png",
        Collections.emptyList(),
        null
    ),
    CARROT_SEED(1, "carrot-seed", 10, "game/crops/Carrot_Seeds.png",
        List.of(TileDescriptionId.SOIL),
        (gameModel, position) -> {
            gameModel.addGrowingCrop(new GrowingCrop(ItemDescriptionId.CARROT), position);
            return null;
        }),
    WATERING_CAN(2, "watering-can", 20, "game/tools/watering_can.png",
        List.of(TileDescriptionId.SOIL),
        (gameModel, position) -> {
            gameModel.water(position);
            return null;
        }),
    HOE(3, "hoe", 30, "game/tools/hoe.png",
        List.of(TileDescriptionId.GRASS),
        (gameModel, position) -> {
            gameModel.getTiles()[position.x][position.y] = TileDescriptionId.SOIL;
            return null;
        }),
    SCYTHE(3, "scythe", 30, "game/tools/scythe.png",
        List.of(TileDescriptionId.SOIL),
        (gameModel, position) -> {
            gameModel.harvest(position);
            return null;
        }),
    ;
    int id;
    String name;
    double value;
    String iconPath;
    List<TileDescriptionId> allowedTiles;
    Function2<GameModel, Point, Null> function;

    private ItemDescriptionId(int id, String name, double value, String iconPath, List<TileDescriptionId> allowedTiles, Function2<GameModel, Point, Null> function) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.iconPath = iconPath;
        this.allowedTiles = allowedTiles;
        this.function = function;
    }

    public String getIconPath() {
        return iconPath;
    }

    public List<TileDescriptionId> getAllowedTiles() {
        return allowedTiles;
    }

    public Function2<GameModel, Point, Null> getFunction() {
        return function;
    }
}
