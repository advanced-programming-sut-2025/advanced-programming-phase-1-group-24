package io.github.stardew.mini.common.Model.Tools;

import io.github.stardew.mini.common.Model.Result;
import java.util.Optional;

/**
 * A record representing the outcome of a fishing attempt.
 * It contains a standard Result (success/failure message) and
 * optionally, data to start the fishing minigame if a fish was hooked.
 */
public record FishingAttemptOutcome(
    Result generalResult,
    Optional<FishingMinigameData> minigameData
) {
}
