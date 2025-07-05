package io.github.stardew.mini.Model.Animals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class CrowFlight {
    public float x;         // Current X position
    public float y;         // Y position to fly at
    public float duration;  // How long the animation lasts
    public float time;      // Elapsed time
    public Animation<TextureRegion> animation; // Flying animation
    public boolean flipped; // Facing left

    public CrowFlight(float screenHeight, Animation<TextureRegion> animation) {
        this.x = Gdx.graphics.getWidth(); // Start off-screen right
        this.y = screenHeight - 100; // Adjust height as needed
        this.duration = MathUtils.random(1.0f, 2.0f); // Seconds to cross the screen
        this.time = 0f;
        this.animation = animation;
        this.flipped = true; // Facing left
    }

    public boolean isFinished() {
        return x + 32 < 0; // Off screen left
    }
}

