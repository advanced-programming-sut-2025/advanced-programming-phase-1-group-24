package org.example.Controller;


import org.example.Model.Menus.LoginMenuCommands;
import org.example.Model.Result;

public class LoginMenuController {

    LoginMenuCommands command;

    public Result createUser(String username, String nickname, String password, String confirmPassword, String email, String gender){}
    public Result pickQuestion(int question, String answer, String confirmAnswer){}
    public Result login(String username, String password, boolean stayLoggedIn){}
    public Result forgetPassword(String username, String answer){}

}
