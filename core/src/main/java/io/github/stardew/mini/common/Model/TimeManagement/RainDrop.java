package io.github.stardew.mini.common.Model.TimeManagement;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.stardew.mini.client.Assets.GameAssetManager;

public class RainDrop {
    float x, y;
    float stateTime;
    public boolean finished;

    public RainDrop(float x, float y) {
        this.x = x;
        this.y = y;
        this.stateTime = 0f;
        this.finished = false;
    }

    public void update(float delta, OrthographicCamera camera) {
        y -= 60 * delta;

        float cameraBottom = camera.position.y - camera.viewportHeight / 2f;
        float totalDropHeight = GameAssetManager.dropFrames.length * GameAssetManager.dropFrames[0].getRegionHeight();

        if (y + totalDropHeight < cameraBottom) {
            finished = true;
        }
    }

    public void render(SpriteBatch batch) {
        float currentY = y;

        for (TextureRegion frame : GameAssetManager.dropFrames) {
            batch.draw(frame, x, currentY);
            currentY -= frame.getRegionHeight(); // draw next frame below
        }
    }
}

