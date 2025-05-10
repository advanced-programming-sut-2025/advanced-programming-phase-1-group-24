package org.example.Model.TimeManagement;

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
}
