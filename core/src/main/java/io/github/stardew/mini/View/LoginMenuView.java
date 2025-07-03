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
import io.github.stardew.mini.Model.User;

import java.util.regex.Matcher;

public class LoginMenuView implements Screen {
    private final LoginMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextField usernameField;
    private TextField passwordField;
    private TextButton loginButton;
    private TextButton forgetPasswordButton;
    private TextButton registerButton;
    private CheckBox stayLoggedInCheckbox;
    private Label errorLabel;

    public LoginMenuView(LoginMenuController controller, Skin skin) {
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

        Label title = new Label("Login", skin, "custom-label");
        title.setFontScale(2);
        usernameField = new TextField("", skin);
        usernameField.getStyle().font.getData().setScale(1.5f);
        usernameField.setMessageText("Username");
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");
        stayLoggedInCheckbox = new CheckBox("Stay logged in", skin);

        loginButton = new TextButton("Login", skin, "custom-button");
        forgetPasswordButton = new TextButton("Forget Password", skin, "custom-button");
        //registerButton = new TextButton("Register", skin);
        errorLabel = new Label("", skin, "custom-label");

        table.add(title).colspan(2).padBottom(20);
        table.row().pad(10);
        table.add(new Label("Username:", skin,"custom-label")).right().padRight(10);
        table.add(usernameField).width(200).height(50);
        table.row().pad(10);
        table.add(new Label("Password:", skin,"custom-label")).right().padRight(10);
        table.add(passwordField).width(200).height(50);
        table.row().pad(20);

        stayLoggedInCheckbox.setTransform(true);
        stayLoggedInCheckbox.setScale(1.5f);
        table.add(stayLoggedInCheckbox).right().padRight(100);
        table.row().pad(10);
        table.add(loginButton).width(400).padRight(10).height(60);

        table.add(forgetPasswordButton).width(400).padRight(10).height(60);
        table.row().pad(10);
        errorLabel.setColor(Color.ROYAL);
        table.add(errorLabel).colspan(2);

        // Listeners
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();

                boolean stayLoggedIn = stayLoggedInCheckbox.isChecked();
                Result result = controller.login(username, password, stayLoggedIn);
                errorLabel.setText(result.message());
                if (result.isSuccessful()) {
                    MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(), skin));
                }
            }
        });
        forgetPasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(() ->
                    MainApp.getInstance().setScreen(
                        new forgetPasswordView(skin)
                    )
                );

            }
        });


    }
        @Override public void render ( float delta){
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(delta);
            stage.draw();
        }


        @Override public void resize ( int width, int height){
            stage.getViewport().update(width, height, true);
        }

        @Override public void pause () {
        }
        @Override public void resume () {
        }
        @Override public void hide () {
        }
        @Override public void dispose () {
            stage.dispose();
            skin.dispose();
        }



    class forgetPasswordView implements Screen {
        private final Skin skin;
        private Stage stage;
        private Table table;
        private TextField usernameField;
        private TextField answerField;
        private TextField newPasswordField;
        private TextButton loginButton;
        private TextButton SecurityQuestionbotton;
        private Label questionLabel;
        private Label errorLabel;

        public forgetPasswordView(Skin skin) {
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

            table.add(new Label("Forget Password", skin,"custom-label")).colspan(2).padBottom(20).row();

            usernameField = new TextField("", skin);
            usernameField.setMessageText("Username");

            SecurityQuestionbotton = new TextButton("SecurityQuestion", skin,"custom-button");
            questionLabel = new Label("", skin, "custom-label");
            answerField = new TextField("", skin);
            answerField.setMessageText("Answer");


            newPasswordField = new TextField("", skin);
            newPasswordField.setMessageText("New Password");


            loginButton = new TextButton("Login", skin, "custom-button");
            errorLabel = new Label("", skin, "custom-label");

//            MainApp app = MainApp.getInstance();
//            User user = app.getUserByUsername(usernameField.getText().trim());
//            if(usernameField.getText().isEmpty()) {
//                System.out.println("User not found");
//            }
            //questionLabel = new Label("Security Question:\n" + user.getSecurityQuestion(), skin);


            table.add(new Label("Username:", skin,"custom-label"));
            table.add(usernameField).width(300).height(40);
            table.row().pad(10);

            table.add(SecurityQuestionbotton).width(500).height(55);
            table.row().pad(10);


            table.add(questionLabel).width(300);
            table.row().pad(10);

            table.add(new Label("Answer The Question:", skin,"custom-label"));
            table.add(answerField).colspan(2).width(300).pad(5).height(40);
            table.row().pad(10);

            table.add(new Label("New Password:", skin,"custom-label"));
            table.add(newPasswordField).width(300).pad(5).height(40);
            table.row().pad(10);

            table.add(loginButton).colspan(2).width(250).pad(10).height(60);
            table.row().pad(10);
            errorLabel.setColor(Color.ROYAL);
            table.add(errorLabel).colspan(2).pad(10);



            loginButton.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    String username = usernameField.getText().trim();
                    String ans = answerField.getText().trim();
                    String newPassword = newPasswordField.getText().trim();
                    Result res = controller.forgetPassword(username, ans, newPassword);
                    errorLabel.setText(res.message());
                    if (res.isSuccessful()) {
                        MainApp.getInstance().setScreen(
                            new LoginMenuView(new LoginMenuController(), skin)
                        );
                    }
                }
            });
            SecurityQuestionbotton.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    String username = usernameField.getText().trim();
                    User user = MainApp.getInstance().getUserByUsername(username);
                    if(username == null || username == "" || username.length() == 0)
                        errorLabel.setText("Please enter a valid username");
                    else if(user == null)
                        errorLabel.setText("Please enter a valid username");
                    else
                    questionLabel.setText("Security Question: " + user.getSecurityQuestion());
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

