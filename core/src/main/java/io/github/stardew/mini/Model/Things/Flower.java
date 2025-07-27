package io.github.stardew.mini.Model.Things;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Flower {
    public Texture texture;
    public float x, y;
    public float timer = 2f; // show for 2 seconds
    public float alpha = 1f;

    public Flower(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        timer -= delta;
        y += 10 * delta; // slowly float up
        alpha = Math.max(0, timer / 2f); // fade out over 2s
    }

    public boolean isFinished() {
        return timer <= 0f;
    }

    public void draw(SpriteBatch batch) {
        Color oldColor = batch.getColor();
        batch.setColor(1f, 1f, 1f, alpha);
        batch.draw(texture, x, y);
        batch.setColor(oldColor); // restore previous color
    }
}
