package io.github.stardew.mini;

import com.badlogic.gdx.*;
import io.github.stardew.mini.control.MainController;
import io.github.stardew.mini.view.asset.Assets;

// 1. Main Application Class
public class StardewMini extends Game {
    public static final int TILE_SIZE = 160;


    @Override
    public void create() {
        Assets.load();
        MainController controller = new MainController(this);
        controller.init();
        controller.run();
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}


