package org.example.Controller;


import org.example.Main;
import org.example.Model.App;
import org.example.Model.Game;
import org.example.Model.Menus.GameMenuCommands;
import org.example.Model.Result;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;
import org.example.Model.Tools.ToolType;
import org.example.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenuController implements MenuController {

    GameMenuCommands command;

    public Result createGame(String users, Scanner scanner) {
        App app = App.getInstance();
        User creator = app.getLoggedInUser();

        if (creator == null)
            return new Result(false, "please login first!");

        // Split usernames and clean empty entries (e.g., if user types extra spaces)
        List<String> usernames = Arrays.stream(users.trim().split("\\s+"))
                .filter(usersString -> !usersString.isEmpty())
                .toList();

        if (usernames.isEmpty())
            return new Result(false, "you must specify at least one username!");

        if (usernames.size() > 3)
            return new Result(false, "you can specify up to 3 usernames!");

        // Check if the creator is already in a game
        for (Game game : app.getActiveGames()) {
            if (game.hasUser(creator))
                return new Result(false, "you are already in another game!");
        }

        ArrayList<User> players = new ArrayList<>();
        players.add(creator); // Add the logged-in user first

        for (String username : usernames) {
            User user = app.getUserByUsername(username);
            if (user == null)
                return new Result(false, "invalid username: " + username);

            // Check if the user is already in a game
            Game game = app.getGameByUser(user);
            if (game != null) {
                return new Result(false, username + " is already in another game!");
            }
            players.add(user);
        }
        for (User player : players) {
            player.updateGameFields();
        }
        // Create and add the game
        Game newGame = new Game(players, creator, creator);
        app.getActiveGames().add(newGame);
        app.setCurrentGame(newGame);

        handleMapSelection(players, scanner);

        return new Result(true, "game created successfully!");
    }

    private void handleMapSelection(List<User> players, Scanner scanner) {
        for (User player : players) {
            System.out.println("hey " + player.getUsername() + " choose between map 1 or map 2");
            boolean hasChosen = false;
            while (!hasChosen) {
                String input = scanner.nextLine().trim();
                Matcher matcher = GameMenuCommands.CHOOSE_MAP.getMatcher(input);
                if (matcher != null) {
                    int mapNumber = Integer.parseInt(matcher.group("mapNumber"));
                    if (mapNumber != 1 && mapNumber != 2) {
                        System.out.println("invalid map number");
                    } else {
                        // Call method to apply map to player ///////////////////////////
                        hasChosen = true;
                    }
                } else {
                    System.out.println("invalid command");
                }
            }
        }
    }

    public Result loadGame() {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        Game savedGameToLoad = app.getGameByUser(user);

        if (savedGameToLoad == null)
            return new Result(false, "no saved game found!");

        savedGameToLoad.setMainPlayer(user);
        app.setCurrentGame(savedGameToLoad);
        return new Result(true, "game loaded successfully!");
    }

    public Result exitGame() {
        App app = App.getInstance();
        User currentUser = app.getLoggedInUser();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null)
            return new Result(false, "no active game to exit!");

        if (!currentGame.getMainPlayer().equals(currentUser))
            return new Result(false, "only the game owner can exit the game!");

        if (!currentGame.getCurrentPlayer().equals(currentUser)) // check if it's their turn
            return new Result(false, "you can only exit the game during your turn!");

        // Save the current game state
        for (User player : currentGame.getPlayers()) {
            player.updateMaxMoney();
        }
        app.saveActiveGames();

        // Exit game: go back to game menu
        app.setCurrentGame(null);
        return new Result(true, "game exited and saved successfully. returning to game menu...");
    }

    public Result startForceTerminateVote(Scanner scanner) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();
        User currentUser = app.getLoggedInUser();

        if (currentUser == null || currentGame == null)
            return new Result(false, "no active game!");

        if (currentGame.isVoteInProgress())
            return new Result(false, "a termination vote is already in progress!");

        // Start the vote and auto-approve for the initiator
        currentGame.setVoteInProgress(true);
        currentGame.getTerminationVotes().clear();
        currentGame.getTerminationVotes().put(currentUser, true);

        return new Result(true, "termination vote started. your vote is recorded as YES.");
    }


    public Result nextTurn(Scanner scanner) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null)
            return new Result(false, "no active game!");

        User pastUser = currentGame.getCurrentPlayer();
        pastUser.resetTurnEnergy();

        currentGame.goToNextTurn();

        User currentUser = currentGame.getCurrentPlayer();

        if (currentGame.isVoteInProgress() && !currentGame.getTerminationVotes().containsKey(currentUser)) {
            return voteToTerminateInteractive(scanner, currentUser);
        }
        handleEndOfDay();

        return new Result(true, "next turn started for " + currentUser.getUsername());
    }

    private Result voteToTerminateInteractive(Scanner scanner, User user) {
        Game currentGame = App.getInstance().getCurrentGame();

        System.out.println("a force terminate vote is in progress. you must vote first. enter 'yes' or 'no':");

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes")) {
                return voteToTerminate(true, user);
            } else if (input.equals("no")) {
                return voteToTerminate(false, user);
            } else {
                System.out.println("invalid vote. enter 'yes' or 'no':");
            }
        }
    }

    public Result voteToTerminate(boolean approve, User user) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null || !currentGame.isVoteInProgress())
            return new Result(false, "no active termination vote!");

        if (!currentGame.isUserTurn(user))
            return new Result(false, "you can only vote during your own turn!");

        if (currentGame.getTerminationVotes().containsKey(user))
            return new Result(false, "you have already voted!");

        currentGame.getTerminationVotes().put(user, approve);

        if (!approve) {
            currentGame.setVoteInProgress(false);
            currentGame.getTerminationVotes().clear();
            return new Result(true, "you voted NO. game termination cancelled.");
        }

        // Check if all players have voted yes
        if (currentGame.getTerminationVotes().size() == currentGame.getPlayers().size()) {
            for (User player : currentGame.getPlayers()) {
                player.updateMaxMoney();
            }
            app.getActiveGames().remove(currentGame);
            app.setCurrentGame(null);
            app.saveActiveGames();
            return new Result(true, "game terminated by unanimous vote. returning to game menu...");
        }

        return new Result(true, "your vote is recorded as YES.");
    }
    private void handleEndOfDay() {
        App app = App.getInstance();
        Game game = app.getCurrentGame();
        if (game.getTimeAndDate().getHour() == 22) {

            for (User user : game.getPlayers()) {
//                if (user.canWalkHome()) {
//                    user.walkHome(); // This should reduce energy and move them to home
//                } else {
//                    user.faint(); // Player faints in place
//                }
            }

            // Skip time to 9 AM
            game.getTimeAndDate().skipToNextMorning();

            // Grow crops and update energy
            for (User user : game.getPlayers()) {
                user.resetEnergyForNewDay();
                //user.getFarm().growCropsOneDay();
               // user.getFarm().generateForageAndMine();
                //user.collectShippingBinProfits();
            }
            //TO DO: update crops and days left to die delete crops and tree

            // Update weather for the new day
           // game.getCurrentWeatherType() = WeatherType.randomWeather();
        }
    }

//    public Result cheatAdvanceTime(int hours){}
//    public Result cheatAdvanceDate(int days){}
    public void strikeRandomFarm() {
    }

    public void CheatStrikeLightening() {
    }

//    public Result showCurrentWeather(){}
//    public Result showPredictedWeather(){}
//    public Result cheatSetWeather(){}
//    public void buildGreenhouse(){}
//    public Result walk(int x, int y){}
//    public Result printMap(int x, int y, int size){}
//    public Result helpReadMap(){}
//    public Result showEnergy(){}
//    public Result cheatEnergySet(int value){}
//    public Result cheatEnergyUnlimited(){}
//    public Result trashInventory(ToolType item, int quantity){}
//    public Result showTradingMenu(){}


    public void startNewDay() {
    }

    public Result printDayOfWeek() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Day of week: " + currentGame.getTimeAndDate().getDayOfWeek());
    }

    public Result printDateTime() {
        Game currentGame = App.getInstance().getCurrentGame();
        TimeAndDate currentDate = currentGame.getTimeAndDate();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Date: " + currentDate.getHour() + " hour of day " + currentDate.getDay());
    }

    public Result printDate() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Day of season: " + currentGame.getTimeAndDate().getDay());

    }

    public Result printHour() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Hour: " + currentGame.getTimeAndDate().getHour());
    }

    public Result printSeason() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Season: " + currentGame.getTimeAndDate().getSeason());

    }


    public Result cheatAdvanceDate(String number) {
    int days = Integer.parseInt(number);
        if (days <= 0) {
            return new Result(false, "invalid number of days!");
        }

        Game game = App.getInstance().getCurrentGame();
        if (game == null) return new Result(false, "no active game!");

        for (int i = 0; i < days; i++) {
            // Force day to end if not already at 22
            game.getTimeAndDate().setHour(22);
            handleEndOfDay(); // Will skip to 9 AM next day and apply effects
        }

        return new Result(true, "advanced time by " + days + " days.");
    }

    public Result cheatAdvanceTime(String number) {
        int hours = Integer.parseInt(number);
        if (hours <= 0) {
            return new Result(false, "invalid number of hours!");
        }

        Game game = App.getInstance().getCurrentGame();
        if (game == null) return new Result(false, "no active game!");

        for (int i = 0; i < hours; i++) {
            game.getTimeAndDate().advanceHour(); // You need to implement this method
            if (game.getTimeAndDate().getHour() == 22) {
                handleEndOfDay();
            }
        }

        return new Result(true, "advanced time by " + hours + " hours.");
    }
}
