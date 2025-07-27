package io.github.stardew.mini.client.View;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.stardew.mini.client.Assets.GameAssetManager;

import java.util.Scanner;
import java.util.function.Consumer;

public interface AppMenu {
    public  void handleCommand(Scanner scanner, Consumer<String> callback);
     default void showErrorDialog(Stage stage, String message) {
        Skin skin = GameAssetManager.skin;

        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                // Optional: Handle result
            }
        };

        dialog.setBackground("window"); // make sure "window" drawable exists in your skin

        Label messageLabel = new Label(message, skin,"custom-label");
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        messageLabel.setFontScale(0.7f); // Optional

        TextButton okButton = new TextButton("OK", skin, "custom-button");
        okButton.pad(10f);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide(); // Keep this to close dialog properly
            }
        });

        Table contentTable = new Table();
        contentTable.defaults().pad(10f);
        contentTable.add(messageLabel).width(stage.getWidth() * 0.5f).row();
        contentTable.add(okButton).center();

        dialog.getContentTable().clear();
        dialog.getContentTable().add(contentTable).expand().fill();

        dialog.setMovable(false);
        dialog.setModal(true);
        dialog.setResizable(false);

        float dialogWidth = stage.getWidth() * 0.4f;
        float dialogHeight = stage.getHeight() * 0.25f;
        dialog.setSize(dialogWidth, dialogHeight);
        dialog.setPosition(
            (stage.getWidth() - dialogWidth) / 2f,
            (stage.getHeight() - dialogHeight) / 2f
        );

        // ✅ Correct fade-in
        stage.addActor(dialog);
        // dialog.addAction(Actions.fadeIn(0.01f));
    }
}
