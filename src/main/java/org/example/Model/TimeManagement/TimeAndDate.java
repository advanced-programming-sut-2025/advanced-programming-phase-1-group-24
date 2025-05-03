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
        dayOfWeek = dayOfWeek.next();

        if (day > 28) {
            day = 1;
            advanceSeason();
        }
    }

    private void advanceSeason() {
        season = season.next();
    }

    public void skipToNextMorning() {
        advanceDay();
        this.hour = 9;
    }


    // Getters (optional)
    public int getHour() {
        return hour;
    }

    public int getDay() {
        return day;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public Season getSeason() {
        return season;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}

