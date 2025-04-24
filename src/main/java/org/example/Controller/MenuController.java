package org.example.Controller;

import org.example.Model.Result;

public interface MenuController {
    Result enterMenu(String menuName);
    default public void menuExit(){}
    default public Result showCurrentMenu(){}
}
