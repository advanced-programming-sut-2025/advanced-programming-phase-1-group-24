package org.example.Model.MapManagement;

public enum TileType {
    EMPTY("E", "\u001B[47m"),          // White background
    LAKE("L", "\u001B[44m"),           // Blue
    GREENHOUSE("G", "\u001B[102m"),    // Bright Green
    WATERCONTAINER("W", "\u001B[46m"), // Cyan
    CAGE("C", "\u001B[43m"),           // Yellow
    BARN("B", "\u001B[41m"),           // Red
    QUARRY("Q", "\u001B[45m"),         // Magenta
    HOUSE("H", "\u001B[105m"),         // Bright Magenta
    GRASS("g", "\u001B[42m"),          // Green
    FARM("F", "\u001B[103m"),          // Bright Yellow
    WALL("w", "\u001B[47m"),           // White
    NPCLAND("N", "\u001B[106m"),       // Bright Cyan
    NPCHOUSE("h", "\u001B[105m"),
    DOOR("D", "\u001B[41m"),           // White
    SHOP("S", "\u001B[42m"),
    SHIPPINGBIN("s","\u001B[41m");           // Green

    private final String letterToPrint;
    private final String backgroundColor;

    TileType(String letterToPrint, String backgroundColor) {
        this.letterToPrint = letterToPrint;
        this.backgroundColor = backgroundColor;
    }

    public String getLetterToPrint() {
        return letterToPrint;
    }

    public String coloredSymbol() {
        return "\u001B[37m" + backgroundColor + letterToPrint + "\u001B[0m";
    }
}