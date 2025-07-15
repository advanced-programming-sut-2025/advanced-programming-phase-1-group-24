package io.github.stardew.mini.Model.Animals;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class CrowFlight {
    public float x;         // Current X position in world coordinates
    public float y;         // Y position in world coordinates
    public float duration;  // How long the animation lasts
    public float time;      // Elapsed time
    public Animation<TextureRegion> animation; // Flying animation
    public boolean flipped; // Facing left

    public CrowFlight(OrthographicCamera camera, Animation<TextureRegion> animation) {
        float screenRight = camera.position.x + camera.viewportWidth / 2f;
        float screenTop = camera.position.y + camera.viewportHeight / 2f;

        this.x = screenRight + 50; // start off-screen right
        this.y = MathUtils.random(screenTop - 150, screenTop - 100); // upper part of screen

        this.duration = MathUtils.random(1.0f, 2.0f); // cross in 1-2 seconds
        this.time = 0f;
        this.animation = animation;
        this.flipped = true;
    }

    public boolean isFinished(float cameraLeft) {
        return x + 64 < cameraLeft; // Off-screen left
    }
}


