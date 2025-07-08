package io.github.stardew.mini.Model;

public enum Avatar {
    Abigail(true),
    Haley(true),
    Shane(false),
    Alex(false),;

    final boolean Gender;
    Avatar(boolean Gender) {
        this.Gender = Gender;
    }
}
