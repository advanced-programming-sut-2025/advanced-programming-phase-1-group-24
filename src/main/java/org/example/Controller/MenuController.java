package org.example.Controller;

import Model.Result;

public interface MenuController {
    Result enterMenu(String menuName);
    default public void menuExit(){}
    default public Result showCurrentMenu(){}
}
