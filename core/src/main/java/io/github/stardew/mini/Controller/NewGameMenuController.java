package io.github.stardew.mini.Controller;

import io.github.stardew.mini.View.NewGameMenuView;

public class NewGameMenuController implements MenuController{
    private NewGameMenuView view;
    public void setView(NewGameMenuView view) {
        this.view = view;
    }

}
