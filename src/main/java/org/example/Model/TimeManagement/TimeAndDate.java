package org.example.Model.TimeManagement;

public class TimeAndDate {
    private String time;
    private String date;
    private DayOfWeek dayOfWeek;
    private Season season;

    public TimeAndDate(String time, String date, DayOfWeek dayOfWeek, Season season) {
        this.time = time;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.season = season;
    }

    public String getTime() { return time; }
    public String getDate() { return date; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public Season getSeason() { return season; }

    public void updateTime(){}


}
