package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.Controller.LoginMenuController;
import io.github.stardew.mini.Controller.MainMenuController;
import io.github.stardew.mini.Controller.SignupMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.GameAssetManager;
import io.github.stardew.mini.Model.Menus.LoginMenuCommands;
import io.github.stardew.mini.Model.Result;

import java.util.regex.Matcher;

public class SignupMenuView implements Screen {
    private final SignupMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextField usernameField;
    private TextField passwordField;
    private TextField confirmField;
    private TextField emailField;
    private TextField nicknameField;
    private SelectBox<String> genderSelect;
    private TextButton registerButton;
    private TextButton loginButton;
    private TextButton generatePasswordButton;
    private Label generatedPasswordLabel;
    private Label errorLabel;

    public SignupMenuView(SignupMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // background
        Texture bg = GameAssetManager.getBackground();
        Image bgImage = new Image(bg);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        // UI Table
        table = new Table(skin);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("Signup", skin, "custom-label");
        title.setFontScale(2);
        usernameField = new TextField("", skin);
        usernameField.getStyle().font.getData().setScale(1.5f);
        usernameField.setMessageText("Username");
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");
        confirmField = new TextField("", skin);
        confirmField.setPasswordMode(true);
        confirmField.setPasswordCharacter('*');
        confirmField.setMessageText("Confirm Password");

        emailField = new TextField("", skin);
        emailField.setMessageText("Email");
        nicknameField = new TextField("", skin);
        nicknameField.setMessageText("Nickname");
        genderSelect = new SelectBox<>(skin);
        genderSelect.setItems("Male", "Female");

        registerButton = new TextButton("Register", skin, "custom-button");
        loginButton = new TextButton("Login", skin, "custom-button");
        generatePasswordButton = new TextButton("Generate Random Password", skin, "custom-button");

        generatedPasswordLabel = new Label("", skin, "custom-label");
        errorLabel = new Label("", skin, "custom-label");

        table.add(title).colspan(2).padBottom(20);
        table.row().pad(10);
        table.add(new Label("Username:", skin,"custom-label")).right().padRight(10);
        table.add(usernameField).width(200).height(40);
        table.row().pad(10);
        table.add(new Label("Password:", skin,"custom-label")).right().padRight(10);
        table.add(passwordField).width(200).height(40);
        table.row().pad(20);
        table.row().pad(10);
        table.add(new Label("Confirm:", skin,"custom-label")).right().padRight(10);
        table.add(confirmField).width(200).height(40);

        table.row().pad(10);
        table.add(new Label("Email:", skin,"custom-label")).right().padRight(10);
        table.add(emailField).width(200).height(40);

        table.row().pad(10);
        table.add(new Label("Nickname:", skin,"custom-label")).right().padRight(10);
        table.add(nicknameField).width(200).height(40);

        table.row().pad(10);
        table.add(new Label("Gender:", skin,"custom-label")).right().padRight(10);
        table.add(genderSelect).width(200).height(40);
        //table.add(registerButton).width(150).height(30);
        table.row().pad(15);
        table.add(generatePasswordButton).colspan(2).width(800).height(70);
        table.row().pad(5);
        table.add(new Label("Generated:", skin,"custom-label")).right().padRight(10);
        generatedPasswordLabel.setColor(Color.ROYAL);
        table.add(generatedPasswordLabel).left();
        table.row().pad(15);
        table.add(registerButton).width(250).colspan(2).height(70);
        table.row().pad(10);
        table.add(loginButton).width(250).colspan(2).height(60);
        table.row().pad(15);
        errorLabel.setColor(Color.ROYAL);
        table.add(errorLabel).colspan(2);

        // Listeners

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                String confirm = confirmField.getText();
                String email = emailField.getText().trim();
                String gender = genderSelect.getSelected();
                String nickname = nicknameField.getText();

                Result result = controller.register(username, password, confirm, nickname, email, gender);
                errorLabel.setText(result.message());
                if (result.isSuccessful()) {
                    Gdx.app.postRunnable(() ->
                            MainApp.getInstance().setScreen(
                                new SecurityQuestionView(skin)
                            )
                    );
                    //MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(), skin));
                }
            }
        });

        generatePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String generated = SignupMenuController.generateStrongRandomPassword();
                passwordField.setText(generated);
                confirmField.setText(generated);
                generatedPasswordLabel.setText(generated);
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setScreen(new LoginMenuView(new LoginMenuController(), skin));
            }
        });




    }

        @Override public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }


    class SecurityQuestionView implements Screen {
        private static final String[] QUESTIONS = new String[]{
            "What is your favorite food?",
            "What is your first pet's name?",
            "What city were you born in?",
            "Amoo chand salete?",
            "Riazi 2 to chand shodi?",
            "In my little pony what is AppleJack's pet name?",
            "How many times did SpongeBob take the driving test?"
        };
        private final Skin skin;
        private Stage stage;
        private Table table;
        private SelectBox<String> questionSelect;
        private TextField answerField, confirmField;
        private TextButton submitButton;
        private Label errorLabel;

        public SecurityQuestionView(Skin skin) {
            this.skin = skin;
        }

        @Override public void show() {
            stage = new Stage(new ScreenViewport());
            Gdx.input.setInputProcessor(stage);
            Texture bg = GameAssetManager.getBackground();
            Image bgImage = new Image(bg);
            bgImage.setFillParent(true);
            stage.addActor(bgImage);

            table = new Table(skin);
            table.setFillParent(true);
            table.center();
            stage.addActor(table);

            table.add(new Label("Choose Security Question", skin,"custom-label")).colspan(2).padBottom(20).row();
            questionSelect = new SelectBox<>(skin);
            questionSelect.setItems(QUESTIONS);
            answerField = new TextField("", skin);
            answerField.setMessageText("Answer");
            confirmField = new TextField("", skin);
            confirmField.setMessageText("Confirm Answer");
            submitButton = new TextButton("Submit", skin,"custom-label");
            errorLabel = new Label("", skin,"custom-label");
            errorLabel.setColor(Color.ROYAL);
            table.add(questionSelect).colspan(2).width(300).pad(10).row();
            table.add(answerField).colspan(2).width(300).pad(5).row();
            table.add(confirmField).colspan(2).width(300).pad(5).row();
            table.add(submitButton).colspan(2).width(150).pad(10).row();
            table.add(errorLabel).colspan(2);

            submitButton.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    String question = questionSelect.getSelected();
                    String ans = answerField.getText().trim();
                    String conf = confirmField.getText().trim();
                    Result res = SignupMenuController.pickQuestion(ans, conf, question);
                    errorLabel.setText(res.message());
                    if (res.isSuccessful()) {
                        MainApp.getInstance().setScreen(
                            new MainMenuView(new MainMenuController(), skin)
                        );
                    }
                }
            });
        }

        @Override public void render(float delta) { Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); stage.act(delta); stage.draw(); }
        @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
        @Override public void pause() {}
        @Override public void resume() {}
        @Override public void hide() {}
        @Override public void dispose() { stage.dispose(); skin.dispose(); }
    }
}
