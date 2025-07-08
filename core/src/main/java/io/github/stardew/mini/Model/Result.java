package io.github.stardew.mini.Model;

public record Result(boolean isSuccessful, String message) {

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public String toString() {
        return message;
    }

    public String getMessage() {
        return message;
    }
}

