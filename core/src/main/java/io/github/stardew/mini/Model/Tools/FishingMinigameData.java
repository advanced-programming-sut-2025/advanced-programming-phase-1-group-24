package io.github.stardew.mini.Model.Tools;

import io.github.stardew.mini.Model.Things.Fish;

/**
 * Record to hold data needed to initiate the fishing minigame.
 * This is returned by FishingPole.useFishingPole when a fish is to be caught.
 */
public record FishingMinigameData(
    Fish hookedFish
) {
}
