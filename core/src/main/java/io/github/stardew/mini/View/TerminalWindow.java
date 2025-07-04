package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TerminalWindow extends Window {
    private TextField inputField;
    private Label outputLabel;
    private TextButton submitButton;
    private AppMenu currentMenu;
    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();

    public TerminalWindow(Skin skin, AppMenu currentMenu) {
        super("Cheat Terminal", skin,"custom-window");
        this.currentMenu = currentMenu;
        this.padTop(40); // Adds more space above title
        initializeUI();
    }


//    private void initializeUI() {
//        // Set up window properties
//        this.setModal(true);
//        this.setMovable(false);
//        this.setResizable(false);
//        this.setSize(gameWidth/2, gameHeight/2);
//        this.setPosition(
//            (Gdx.graphics.getWidth() - this.getWidth()) / 2,
//            (Gdx.graphics.getHeight() - this.getHeight()) / 2
//        );
//
//        // Create UI elements
//        Table table = new Table();
//        table.pad(10).defaults().expandX().fillX();
//
//        outputLabel = new Label("", getSkin());
//        outputLabel.setWrap(true);
//
//        inputField = new TextField("", getSkin());
//        inputField.setMessageText("Enter cheat command here...");
//
//        submitButton = new TextButton("Execute", getSkin(),"custom-button");
//
//        float buttonWidth = (float) gameWidth / 4;
//        float buttonHeight = (float) gameHeight / 8;
//        float bottomPad = (float) gameHeight / 30;
//
//        // Add elements to table
//        table.add(outputLabel).expandY().top().row();
//        table.add(inputField).width(buttonWidth).height(buttonHeight).row();
//        table.add(submitButton).width(buttonWidth ).height(buttonHeight);
//
//        this.add(table);
//
//        // Add listeners
//        submitButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                executeCommand();
//            }
//        });
//
//        inputField.setTextFieldListener(new TextField.TextFieldListener() {
//            @Override
//            public void keyTyped(TextField textField, char c) {
//                if (c == '\n' || c == '\r') {
//                    executeCommand();
//                }
//            }
//        });
//    }
private void initializeUI() {
    // Set window size to a third of the screen
    float width = Gdx.graphics.getWidth() * 0.6f;
    float height = Gdx.graphics.getHeight() * 0.5f;
    this.setSize(width, height);
    this.setPosition(
        (Gdx.graphics.getWidth() - width) / 2f,
        (Gdx.graphics.getHeight() - height) / 2f
    );

    this.setModal(true);
    this.setMovable(false);
    this.setResizable(false);

    // UI Elements
    Table table = new Table();
    table.pad(20).defaults().expandX().fillX().space(10);

    outputLabel = new Label("", getSkin(), "custom-label");
    outputLabel.setWrap(true);
    outputLabel.setAlignment(Align.topLeft);

    ScrollPane scrollPane = new ScrollPane(outputLabel, getSkin());
    scrollPane.setFadeScrollBars(false);
    scrollPane.setScrollingDisabled(true, false);
    scrollPane.setScrollbarsOnTop(true);

    inputField = new TextField("", getSkin());
    inputField.setMessageText("Enter cheat command...");
    inputField.setAlignment(Align.left);

    submitButton = new TextButton("Execute", getSkin(), "custom-button");

    float buttonWidth = (float) gameWidth / 4;
    float buttonHeight = (float) gameHeight / 16;
    float bottomPad = (float) gameHeight / 30;
    // Assemble UI
    table.add(scrollPane).height(height * 0.5f).expandY().fill().row();
    table.add(inputField).height(buttonHeight).row();
    table.add(submitButton).height(buttonHeight).width(buttonWidth).center();

    this.add(table).expand().fill();

    // Listeners
    submitButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            executeCommand();
        }
    });

    inputField.setTextFieldListener((textField, c) -> {
        if (c == '\n' || c == '\r') {
            executeCommand();
        }
    });

}
//    public void createUI() {
//        Skin skin = GameAssetManager.skin;
//        stage = new Stage(new FitViewport(gameWidth, gameHeight));
//        Gdx.input.setInputProcessor(stage);
//
//        table = new Table();
//        table.setFillParent(true);
//
//        background = GameAssetManager.getBackground();
//
//        Label titleLabel = new Label("NEW GAME MENU", skin);
//        titleLabel.setFontScale(2.5f);
//        titleLabel.setAlignment(Align.center);
//        titleLabel.setStyle(new Label.LabelStyle(skin.getFont("custom-font"), Color.GOLD));
//
//        nameInput = new TextField("", skin);
//        nameInput.getStyle().font.getData().setScale(2.2f);
//        nameInput.setAlignment(Align.center);
//        nameInput.setMessageText("Enter username");
//
//        addPlayerButton = new TextButton("Add Player", skin, "custom-button");
//        startGameButton = new TextButton("Start Game", skin, "custom-button");
//        startGameButton.setDisabled(true); // disabled initially
//
//        errorLabel = new Label("", skin);
//        errorLabel.setColor(Color.RED);
//
//        float buttonWidth = (float) gameWidth / 4;
//        float buttonHeight = (float) gameHeight / 8;
//        float bottomPad = (float) gameHeight / 30;
//
//        table.add(titleLabel).colspan(2).padBottom(bottomPad * 2).row();
//        table.add(nameInput).width(buttonWidth).height(buttonHeight / 2).padBottom(bottomPad);
//        table.add(addPlayerButton).width(buttonWidth ).height(buttonHeight).padBottom(bottomPad).row();
//        table.add(errorLabel).colspan(2).padBottom(bottomPad).row();
//
//        // Placeholder for player list display
//        updatePlayerListUI();
//
//        table.add(startGameButton).colspan(2).padTop(bottomPad * 2).width(buttonWidth).height(buttonHeight).row();
//
//        // Add listeners
//        addPlayerButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                String username = nameInput.getText().trim();
//                playerNames.add(username);
//                nameInput.setText("");
//                updatePlayerListUI();
//            }
//        });
//
//        startGameButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                String usersString = String.join(" ", playerNames);
//                Result result = controller.createGame(usersString);
//                if(!result.isSuccessful()){
//                    showErrorDialog(stage,result.message());
//                    usersString = "";
//                    playerNames.clear();
//                    updatePlayerListUI();
//                } else {
//                    MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu);
//                }
//            }
//        });
//        stage.addActor(table);
//    }

    private void executeCommand() {
        String command = inputField.getText().trim();
        if (!command.isEmpty()) {
            // Simulate the Scanner input handling from your original code
            String result = processCommand(command);
            outputLabel.setText(result);
            inputField.setText("");
        }
    }

//    private String processCommand(String command) {
//        // Create a Scanner that reads from our string
//        Scanner scanner = new Scanner(new ByteArrayInputStream(command.getBytes()));
//
//        try {
//            // Handle the command using your existing menu logic
//            currentMenu.handleCommand(scanner);
//
//            // In your original code, output goes to System.out
//            // You'll need to modify your controllers to return strings instead of printing
//            // Or capture System.out output (more complex)
//
//            // For now, just return the command as if it was processed
//            return "Executed: " + command;
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        } finally {
//            scanner.close();
//        }
//    }
    private String processCommand(String command) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(output));

        Scanner scanner = new Scanner(new ByteArrayInputStream(command.getBytes()));

        try {
            currentMenu.handleCommand(scanner);
            return output.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            scanner.close();
            System.setOut(oldOut);
        }
    }


    public TextField getInputField() {
        return inputField;
    }
}
