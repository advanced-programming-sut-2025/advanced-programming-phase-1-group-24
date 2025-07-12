package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Assets.InventoryAssets;

public class FishingMinigameDialog extends Group {

    // Visual elements
    private Table containerTable;
    private Image backgroundBar;
    private Image fishIcon;
    private Image greenBar;
    private ProgressBar catchProgressBar;

    // Minigame state variables
    private float fishPosition; // 0 to 1, representing vertical position in the bar
    private float fishSpeed;    // How fast the fish moves
    private float greenBarPosition; // 0 to 1, representing vertical position of the player's bar
    private float greenBarHeight; // Relative height of the green bar (0 to 1)
    private float greenBarSpeed; // How fast the green bar moves up/down
    private boolean isGreenBarMovingUp; // True if player is holding input
    private float catchProgress; // 0 to 100 for the ProgressBar

    // Callback for when the minigame ends
    public interface FishingMinigameCallback {
        void onMinigameEnd(boolean caughtSuccessfully, boolean perfectCatch);
    }
    private FishingMinigameCallback callback;
    private boolean perfectCatchTracker; // To track if the fish ever left the bar for "perfect"

    // Constants for minigame scaling and appearance
    private static final float BAR_WIDTH = 50f; // Width of the fishing bar
    private static final float BAR_HEIGHT = 400f; // Height of the fishing bar
    private static final float FISH_ICON_SIZE = 32f;
    private static final float GREEN_BAR_MIN_HEIGHT_PX = 60f; // Minimum height of green bar in pixels
    private static final float GREEN_BAR_MAX_HEIGHT_LVL_FACTOR = 0.05f; // How much height scales with fishing level
    private static final float GREEN_BAR_LIFT_ACCELERATION = 550f; // Pixels per second squared
    private static final float GREEN_BAR_GRAVITY = 400f; // Pixels per second squared
    private static final float FISH_BASE_SPEED = 100f; // Original was 150f. Lowering makes fish slower.
    private static final float CATCH_PROGRESS_SPEED = 20f; // Original was 30f. Higher makes catch bar fill faster.
    private static final float CATCH_DECAY_SPEED = 3f; // Original was 20f. Lower makes catch bar deplete slower.
    private static final float FISH_RANDOM_ACCELERATION = 80f; // NEW: Constant for random fish movement

    private ShapeRenderer shapeRenderer;
    private boolean isMinigameActive;

    public FishingMinigameDialog() {
        this.setVisible(false);
        this.setSize(BAR_WIDTH + 100, BAR_HEIGHT + 100);
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);

        shapeRenderer = new ShapeRenderer();

        containerTable = new Table();
        containerTable.setSize(BAR_WIDTH, BAR_HEIGHT);
        containerTable.setPosition(this.getWidth() / 2 - BAR_WIDTH / 2, 50);
        this.addActor(containerTable);

        backgroundBar = new Image(GameAssetManager.FLOORING_06);
        backgroundBar.setSize(BAR_WIDTH, BAR_HEIGHT);
        containerTable.addActor(backgroundBar);

        greenBar = new Image(InventoryAssets.highlightedSlot);
        greenBar.setSize(BAR_WIDTH, GREEN_BAR_MIN_HEIGHT_PX);
        containerTable.addActor(greenBar);

        fishIcon = new Image(new Texture("Fish/Perch.png"));
        fishIcon.setSize(FISH_ICON_SIZE, FISH_ICON_SIZE);
        containerTable.addActor(fishIcon);

        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = new TextureRegionDrawable(new TextureRegion(GameAssetManager.pixel)).tint(Color.DARK_GRAY);
        style.knobBefore = new TextureRegionDrawable(new TextureRegion(GameAssetManager.pixel)).tint(Color.GREEN);
        catchProgressBar = new ProgressBar(0, 100, 1, true, style);
        catchProgressBar.setSize(100f, BAR_HEIGHT);
        catchProgressBar.setPosition(containerTable.getX() + BAR_WIDTH + 10, containerTable.getY());
        this.addActor(catchProgressBar);
    }

    public void setMinigameCallback(FishingMinigameCallback callback) {
        this.callback = callback;
    }

    /**
     * Starts the fishing minigame.
     * @param fishingLevel The player's current fishing skill level.
     */
    public void startMinigame(int fishingLevel /* Removed float difficulty from signature */) { // MODIFIED: Removed difficulty parameter
        this.setVisible(true);
        isMinigameActive = true;

        fishPosition = 0.5f;
        greenBarPosition = 0.5f;
        greenBarSpeed = 0f;
        isGreenBarMovingUp = false;
        catchProgress = 0;
        perfectCatchTracker = true;

        // Removed assignment of fishDifficulty

        // Randomize initial fish speed and direction (using fixed speed/acceleration for no difficulty)
        fishSpeed = FISH_BASE_SPEED * (MathUtils.randomBoolean() ? 1 : -1); // MODIFIED: Simplified initial speed

        // Adjust green bar height based on fishing level
        greenBarHeight = (GREEN_BAR_MIN_HEIGHT_PX + (fishingLevel * GREEN_BAR_MAX_HEIGHT_LVL_FACTOR * BAR_HEIGHT)) / BAR_HEIGHT;
        greenBarHeight = MathUtils.clamp(greenBarHeight, 0.1f, 0.8f);

        updateVisuals();
    }

    public void stopMinigame() {
        isMinigameActive = false;
        this.setVisible(false);
    }

    public boolean isMinigameActive() {
        return isMinigameActive;
    }

    public void setGreenBarMovingUp(boolean movingUp) {
        this.isGreenBarMovingUp = movingUp;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isMinigameActive) return;

        // 1. Update Green Bar Movement
        if (isGreenBarMovingUp) {
            greenBarSpeed += GREEN_BAR_LIFT_ACCELERATION * delta;
        } else {
            greenBarSpeed -= GREEN_BAR_GRAVITY * delta;
        }
        greenBarSpeed = MathUtils.clamp(greenBarSpeed, -500f, 500f);

        greenBarPosition += (greenBarSpeed / BAR_HEIGHT) * delta;
        greenBarPosition = MathUtils.clamp(greenBarPosition, 0f, 1f - greenBarHeight);

        // 2. Update Fish Movement (Simplified without difficulty factor)
        fishSpeed += MathUtils.random(-FISH_RANDOM_ACCELERATION, FISH_RANDOM_ACCELERATION) * delta; // MODIFIED: Uses fixed random acceleration
        fishSpeed = MathUtils.clamp(fishSpeed, -FISH_BASE_SPEED, FISH_BASE_SPEED); // MODIFIED: Clamped to base speed

        fishPosition += (fishSpeed / BAR_HEIGHT) * delta;
        fishPosition = MathUtils.clamp(fishPosition, 0f, 1f - (FISH_ICON_SIZE / BAR_HEIGHT));

        if (fishPosition <= 0 || fishPosition >= 1f - (FISH_ICON_SIZE / BAR_HEIGHT)) {
            fishSpeed *= -1;
            fishPosition = MathUtils.clamp(fishPosition, 0f, 1f - (FISH_ICON_SIZE / BAR_HEIGHT));
        }

        // 3. Update Catch Progress
        float greenBarTop = greenBarPosition + greenBarHeight;
        float fishBottom = fishPosition;
        float fishTop = fishPosition + (FISH_ICON_SIZE / BAR_HEIGHT);

        boolean fishInGreenBar = (fishBottom >= greenBarPosition && fishBottom < greenBarTop) ||
            (fishTop > greenBarPosition && fishTop <= greenBarTop) ||
            (greenBarPosition >= fishBottom && greenBarPosition < fishTop);

        if (fishInGreenBar) {
            catchProgress += CATCH_PROGRESS_SPEED * delta;
        } else {
            catchProgress -= CATCH_DECAY_SPEED * delta;
            perfectCatchTracker = false;
        }
        catchProgress = MathUtils.clamp(catchProgress, 0, 100);

        updateVisuals();

        // Check for minigame completion/failure
        if (catchProgress >= 100) {
            stopMinigame();
            if (callback != null) {
                callback.onMinigameEnd(true, perfectCatchTracker);
            }
        } else if (catchProgress <= 0 && !fishInGreenBar) {
            stopMinigame();
            if (callback != null) {
                callback.onMinigameEnd(false, false);
            }
        }
    }

    private void updateVisuals() {
        greenBar.setPosition(
            (BAR_WIDTH / 2) - (greenBar.getWidth() / 2),
            greenBarPosition * BAR_HEIGHT
        );
        greenBar.setHeight(greenBarHeight * BAR_HEIGHT);

        fishIcon.setPosition(
            (BAR_WIDTH / 2) - (FISH_ICON_SIZE / 2),
            fishPosition * BAR_HEIGHT
        );

        catchProgressBar.setValue(catchProgress);
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        if (!isVisible()) return;
        super.draw(batch, parentAlpha);
    }
}
