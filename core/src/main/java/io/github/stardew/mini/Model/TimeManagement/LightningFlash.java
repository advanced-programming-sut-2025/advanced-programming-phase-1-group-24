package io.github.stardew.mini.Model.TimeManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class LightningFlash {
    private float alpha = 1f;
    private float duration = 0.3f; // seconds
    private float timer = 0f;
    private boolean active = false;
    public int scheduledTime = -1;

    public void trigger() {
        alpha = 1f;
        timer = 0f;
        active = true;
    }

    public void update(float delta) {
        if (!active) return;
        timer += delta;
        alpha = Math.max(0, 1f - (timer / duration));
        if (alpha <= 0f) active = false;
    }

    public boolean isActive() {
        return active;
    }

    public float getAlpha() {
        return alpha;
    }
}
