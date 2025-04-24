package org.example.Model.TimeManagement;

public class TimeAndDate {
    private int hour; // 0 - 23
    private int day;  // 1 - 30 (or similar)
    private DayOfWeek dayOfWeek;
    private Season season;

    // Constructor example
    public TimeAndDate(int hour, int day, DayOfWeek dayOfWeek, Season season) {
        this.hour = hour;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.season = season;
    }

    public void advanceHour() {
        hour++;
        if (hour >= 24) {
            hour = 0;
            advanceDay();
        }
    }

    private void advanceDay() {
        day++;
        dayOfWeek = dayOfWeek.next(); // Assuming you have a next() method in DayOfWeek enum

        if (day > 30) { // or however many days per month you want
            day = 1;
            advanceSeason();
        }
    }

    private void advanceSeason() {
        season = season.next(); // Assuming Season enum has a next() method
    }

    // Getters (optional)
    public int getHour() { return hour; }
    public int getDay() { return day; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public Season getSeason() { return season; }
}

