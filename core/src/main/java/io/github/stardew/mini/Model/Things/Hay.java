package io.github.stardew.mini.Model.Things;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.stardew.mini.Model.Assets.GameAssetManager;

public class Hay {
    public Texture texture;
    public float x, y;
    private float time = 0f;
    private final float duration = 2f;
    private boolean finished = false;

    public Hay(Texture texture, float x, float y) {
        this.texture = texture;
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
    public boolean isFinished() {
        return finished;
    }

    public void draw(SpriteBatch batch) {
        float scale = 1f + (0.5f * (1f - time / duration)); // grow slightly
        float size = 45 * scale;
        batch.setColor(1, 1, 1, 1f - (time / duration));
        batch.draw(texture, x, y, size, size);
        batch.setColor(Color.WHITE);
    }
}
