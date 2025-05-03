package org.example.Model;

public record Result(boolean isSuccessful, String message) {

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public String toString() {
        return message;
    }
}

