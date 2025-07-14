package io.github.stardew.mini.Controller;

import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplate;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Growables.ForagingCropType;
import io.github.stardew.mini.Model.Growables.GrowableFactory;
import io.github.stardew.mini.Model.Growables.TreeType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.GameMenuCommands;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Places.*;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.*;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.View.PreGameMenuView;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


public class PreGameMenuController implements MenuController{
    private PreGameMenuView view;
    public void setView(PreGameMenuView view) {
        this.view = view;
    }
    public Result loadGame() {
        MainApp app = MainApp.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        Game savedGameToLoad = app.getGameByUser(user);

        if (savedGameToLoad == null)
            return new Result(false, "no saved game found!");

        savedGameToLoad.setMainPlayer(user);
        savedGameToLoad.reloadExtraData();
        app.setCurrentGame(savedGameToLoad);
       // app.getCurrentGame().reloadExtraData();
        //app.setCurrentMenu(Menu.GameMenu);
        return new Result(true, "game loaded successfully!");
    }
}
