package io.github.stardew.mini.common.Model.TimeManagement;

public enum DayOfWeek {
    Sunday,
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    public DayOfWeek next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
    public static DayOfWeek fromString(String value) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().equalsIgnoreCase(value)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid DayOfWeek: " + value);
    }
}
