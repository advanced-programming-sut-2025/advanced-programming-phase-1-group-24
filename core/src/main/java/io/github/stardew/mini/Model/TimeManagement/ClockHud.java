package io.github.stardew.mini.Model.TimeManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.stardew.mini.Model.Assets.GameAssetManager;

public class ClockHud {

    private Stage stage;
    private Group clockGroup;
    private Image clockBg, arrow, weather, season;
    private Label dateLabel, timeLabel, moneyLabel;


    private float scale = 4f;

    public ClockHud(Stage stage) {
        this.stage = stage;

        createClock();
        stage.addActor(clockGroup);
        updatePosition(); // top-right
    }

    private void createClock() {
        clockGroup = new Group();

        clockBg = new Image(new TextureRegionDrawable(GameAssetManager.CLOCK_MAIN));
        clockBg.setSize(72 * scale, 59 * scale);
        clockGroup.addActor(clockBg);

        arrow = new Image(new TextureRegionDrawable(GameAssetManager.CLOCK_ARROW));
        arrow.setSize(8 * scale, 18 * scale);
        arrow.setOrigin(arrow.getWidth() / 2f, 0);
        arrow.setPosition(clockBg.getWidth() * 0.3082f - arrow.getWidth() / 2,
            clockBg.getHeight() / 2f - arrow.getHeight() / 2 + 65);
        clockGroup.addActor(arrow);

        BitmapFont font = GameAssetManager.customFont;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        font.getData().setScale(0.6f);

        dateLabel = new Label("Sat 1", labelStyle);
        dateLabel.setPosition(clockBg.getWidth() * 0.45f, (clockBg.getHeight() * 0.85f) - 10);
        clockGroup.addActor(dateLabel);

        timeLabel = new Label("9 a.m.", labelStyle);
        timeLabel.setPosition(clockBg.getWidth() * 0.50f, (clockBg.getHeight() * 0.55f) - 30);
        clockGroup.addActor(timeLabel);

        moneyLabel = new Label("0", labelStyle);
        moneyLabel.setPosition(clockBg.getWidth() * 0.26f, (clockBg.getHeight() * 0.15f) - 20);
        clockGroup.addActor(moneyLabel);

        float width = 300;
        float height = 220;
        weather = new Image(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[7]));
        weather.setSize(width * 0.180f, height * 0.200f);
        weather.setPosition(clockBg.getX() - 3 + 0.405f * width , clockBg.getY() + 9 + 0.55f * height);
        clockGroup.addActor(weather);

        season = new Image(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[3]));
        season.setSize(weather.getWidth(), weather.getHeight());
        season.setPosition(weather.getX() - 3 + 0.33f * width , weather.getY() + 5);
        clockGroup.addActor(season);

    }

    private void updatePosition() {
        float x = Gdx.graphics.getWidth() - clockBg.getWidth() - 10;
        float y = Gdx.graphics.getHeight() - clockBg.getHeight() - 10;
        clockGroup.setPosition(x, y);
    }

    public void updateTime(int hour, String timeStr) {
        float angle = 180f - (180f * (hour - 9) / 13f);
        arrow.setRotation(angle);

        timeLabel.setText(timeStr);
    }

    public void updateDate(String dateStr) {
        dateLabel.setText(dateStr);
    }

    public void updateMoney(int money) {
        moneyLabel.setText(String.valueOf(money));
    }

    public void updateWeather(WeatherType weatherType) {
        switch (weatherType) {
            case SUNNY -> weather.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[7]));
            case RAIN -> weather.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[6]));
            case SNOW -> weather.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[9]));
            case STORM -> weather.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[11]));
        }

    }

    public void updateSeason(Season seasonType) {
        switch (seasonType) {
            case SPRING -> season.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[3]));
            case SUMMER -> season.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[1]));
            case AUTUMN -> season.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[10]));
            case WINTER -> season.setDrawable(new TextureRegionDrawable(GameAssetManager.ClOCK_MANNERS[4]));
        }

    }


    public void draw(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        updatePosition();
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }
}
