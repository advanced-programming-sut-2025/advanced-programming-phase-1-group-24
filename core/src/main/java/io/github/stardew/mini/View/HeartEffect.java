package io.github.stardew.mini.View;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class HeartEffect {
    private final float x;
    private float y;
    private float time = 0f;
    private final float duration = 2f;
    private boolean finished = false;

    public HeartEffect(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        time += delta;
        if (time >= duration) {
            finished = true;
        }
        y += 30 * delta;
    }

    public void draw(SpriteBatch batch) {
        float alpha = 1f - (time / duration);
        Color old = batch.getColor();
        batch.setColor(1, 1, 1, alpha);
        batch.setColor(old);
    }
}
