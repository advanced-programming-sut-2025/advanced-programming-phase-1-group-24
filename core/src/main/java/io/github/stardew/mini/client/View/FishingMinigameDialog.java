package io.github.stardew.mini.client.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.math.MathUtils;

import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.Assets.InventoryAssets;
import io.github.stardew.mini.common.Model.Things.FishMovementType;
import io.github.stardew.mini.common.Model.Things.Fish;
import io.github.stardew.mini.common.Model.Things.FishType.RarenessType;

public class FishingMinigameDialog extends Group {

    // Visual elements
    private Table containerTable;
    private Image backgroundBar;
    private Image fishIcon;
    private Image greenBar;
    private ProgressBar catchProgressBar;
    private TextButton cancelButton;
    private TextButton sonarBobberButton;
    private Image sonarDisplaySlot;

    // Minigame state variables
    private float fishPosition;
    private float fishSpeed;
    private float greenBarPosition;
    private float greenBarHeight;
    private float greenBarSpeed;
    private boolean isGreenBarMovingUp;
    private float catchProgress;
    private FishMovementType currentMovementType;
    private float fishMovementTimer;
    private Fish currentFishForSonar;

    // Callback for when the minigame ends
    public interface FishingMinigameCallback {
        void onMinigameEnd(boolean caughtSuccessfully, boolean perfectCatch);
    }
    private FishingMinigameCallback callback;
    private boolean perfectCatchTracker;

    private static final float BAR_WIDTH = 50f;
    private static final float BAR_HEIGHT = 400f;
    private static final float FISH_ICON_SIZE = 32f;
    private static final float GREEN_BAR_MIN_HEIGHT_PX = 60f;
    private static final float GREEN_BAR_MAX_HEIGHT_LVL_FACTOR = 0.05f;

    private static final float GREEN_BAR_LIFT_ACCELERATION = 700f;
    private static final float GREEN_BAR_GRAVITY = 350f;

    private static final float BASE_FISH_SPEED_PX_PER_SEC = 150f;
    private static final float BASE_FISH_ACCELERATION_PX_PER_SEC2 = 100f;

    private static final float CATCH_PROGRESS_SPEED = 25f;
    private static final float CATCH_DECAY_SPEED = 3f;

    private static final float FISH_MOVEMENT_UPDATE_INTERVAL = 0.5f;

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

        cancelButton = new TextButton("Cancel", GameAssetManager.skin, "custom-button");
        cancelButton.setSize(100, 40);
        cancelButton.setColor(Color.RED);
        cancelButton.setPosition(this.getWidth() / 2 - cancelButton.getWidth() / 2, 10);
        this.addActor(cancelButton);

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isMinigameActive) {
                    stopMinigame();
                    if (callback != null) {
                        callback.onMinigameEnd(false, false);
                    }
                }
            }
        });

        sonarBobberButton = new TextButton("Sonar Bobber", GameAssetManager.skin, "custom-button");
        sonarBobberButton.setSize(100, 40);
        sonarBobberButton.setColor(Color.BLUE);
        sonarBobberButton.setPosition(this.getWidth() / 2 - sonarBobberButton.getWidth() / 2, cancelButton.getY() - sonarBobberButton.getHeight() - 5);
        this.addActor(sonarBobberButton);

        sonarDisplaySlot = new Image(new TextureRegionDrawable(GameAssetManager.pixel));
        sonarDisplaySlot.setColor(Color.DARK_GRAY);
        sonarDisplaySlot.setSize(FISH_ICON_SIZE * 2, FISH_ICON_SIZE * 2);
        sonarDisplaySlot.setPosition(this.getWidth() / 2 - sonarDisplaySlot.getWidth() / 2, sonarBobberButton.getY() - sonarDisplaySlot.getHeight() - 5);
        this.addActor(sonarDisplaySlot);

        sonarBobberButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isMinigameActive && currentFishForSonar != null) {
                    Texture fishTexture = currentFishForSonar.getType().getTexture();
                    if (fishTexture != null) {
                        sonarDisplaySlot.setDrawable(new TextureRegionDrawable(fishTexture));
                        sonarDisplaySlot.setColor(Color.WHITE);
                    } else {
                        sonarDisplaySlot.setDrawable(new TextureRegionDrawable(new Texture("Fish/Perch.png")));
                        sonarDisplaySlot.setColor(Color.WHITE);
                    }
                }
            }
        });

        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.background = new TextureRegionDrawable(new TextureRegion(GameAssetManager.pixel)).tint(Color.DARK_GRAY);
        style.knobBefore = new TextureRegionDrawable(new TextureRegion(GameAssetManager.pixel)).tint(Color.GREEN);
        catchProgressBar = new ProgressBar(0, 100, 1, true, style);
        catchProgressBar.setSize(BAR_WIDTH, BAR_HEIGHT);
        catchProgressBar.setPosition(containerTable.getX() + BAR_WIDTH + 10, containerTable.getY());
        this.addActor(catchProgressBar);
    }

    public void setMinigameCallback(FishingMinigameCallback callback) {
        this.callback = callback;
    }

    public void startMinigame(int fishingLevel, FishMovementType movementType, Fish fishToCatch) {
        this.setVisible(true);
        isMinigameActive = true;

        this.currentFishForSonar = fishToCatch;

        sonarDisplaySlot.setDrawable(new TextureRegionDrawable(GameAssetManager.pixel));
        sonarDisplaySlot.setColor(Color.DARK_GRAY);

        if (fishToCatch != null && fishToCatch.getType().getRareness() == RarenessType.LEGENDARY) {
            fishIcon.setDrawable(new TextureRegionDrawable(new Texture("Fish/Legendary_Fish.png")));
            fishIcon.setSize(FISH_ICON_SIZE * 1.5f , FISH_ICON_SIZE * 1.5f);
        }

        fishPosition = 0.5f;

        greenBarHeight = (GREEN_BAR_MIN_HEIGHT_PX + (fishingLevel * GREEN_BAR_MAX_HEIGHT_LVL_FACTOR * BAR_HEIGHT)) / BAR_HEIGHT;
        greenBarHeight = MathUtils.clamp(greenBarHeight, 0.1f, 0.8f);

        greenBarPosition = fishPosition + (FISH_ICON_SIZE / BAR_HEIGHT / 2f) - (greenBarHeight / 2f);
        greenBarPosition = MathUtils.clamp(greenBarPosition, 0f, 1f - greenBarHeight);

        greenBarSpeed = 0f;
        isGreenBarMovingUp = false;
        catchProgress = 0;
        perfectCatchTracker = true;
        fishMovementTimer = 0f;

        this.currentMovementType = movementType;
        fishSpeed = BASE_FISH_SPEED_PX_PER_SEC * movementType.getBaseMaxSpeedFactor() * (MathUtils.randomBoolean() ? 1 : -1);

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

        // 2. Update Fish Movement
        fishMovementTimer += delta;
        if (fishMovementTimer >= FISH_MOVEMENT_UPDATE_INTERVAL) {
            fishMovementTimer -= FISH_MOVEMENT_UPDATE_INTERVAL;

            float randomAccelerationRange = BASE_FISH_ACCELERATION_PX_PER_SEC2 * currentMovementType.getBaseRandomAccelerationFactor();
            float maxFishSpeed = BASE_FISH_SPEED_PX_PER_SEC * currentMovementType.getBaseMaxSpeedFactor();

            float currentRandomAcceleration = randomAccelerationRange * (1.0f - currentMovementType.getPredictabilityFactor() + MathUtils.random(0f, 1.0f) * currentMovementType.getPredictabilityFactor());

            float centerPull = (0.5f - fishPosition) * (BASE_FISH_ACCELERATION_PX_PER_SEC2 * 0.1f);


            switch (currentMovementType) {
                case MIXED:
                    fishSpeed += MathUtils.random(-currentRandomAcceleration, currentRandomAcceleration) + centerPull;
                    break;
                case SMOOTH:
                    fishSpeed += MathUtils.random(-currentRandomAcceleration * 0.5f, currentRandomAcceleration * 0.5f) + centerPull;
                    break;
                case SINKER:
                    fishSpeed += MathUtils.random(-currentRandomAcceleration, currentRandomAcceleration) + (BASE_FISH_ACCELERATION_PX_PER_SEC2 * 0.2f) + centerPull;
                    break;
                case FLOATER:
                    fishSpeed += MathUtils.random(-currentRandomAcceleration, currentRandomAcceleration) - (BASE_FISH_ACCELERATION_PX_PER_SEC2 * 0.2f) + centerPull;
                    break;
                case DART:
                    fishSpeed += MathUtils.random(-randomAccelerationRange * 1.5f, randomAccelerationRange * 1.5f) + centerPull;
                    maxFishSpeed *= 1.2f;
                    break;
            }
            fishSpeed = MathUtils.clamp(fishSpeed, -maxFishSpeed, maxFishSpeed);
        }


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
            (BAR_WIDTH / 2) - (fishIcon.getWidth() / 2),
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
