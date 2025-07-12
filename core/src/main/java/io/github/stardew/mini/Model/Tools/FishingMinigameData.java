package io.github.stardew.mini.Model.Tools;

import io.github.stardew.mini.Model.Things.Fish;
import io.github.stardew.mini.Model.Things.FishMovementType;

/**
 * Record to hold data needed to initiate the fishing minigame.
 * This is returned by FishingPole.useFishingPole when a fish is to be caught.
 */
public record FishingMinigameData(
    Fish hookedFish,
    FishMovementType movementType
) {
}
