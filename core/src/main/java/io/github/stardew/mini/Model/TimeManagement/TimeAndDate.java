package io.github.stardew.mini.Model.TimeManagement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.Reccepies.Machine;
import io.github.stardew.mini.Model.NPCManagement.NPC;
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")

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

    public TimeAndDate() {
    }

    public void advanceHour() {
        hour++;
        if (hour >= 24) {
            hour = 0;
            advanceDay();
        }
        //MainApp.getInstance().getCurrentGame().getCurrentPlayer().handleSpecialFoodsEffects();
        //Machine.updateMachines();
//        if (MainApp.getInstance().getCurrentGame() != null) {
//            for (NPC npc : MainApp.getInstance().getCurrentGame().getNpcs()) {
//                npc.updateRoutine(MainApp.getInstance().getCurrentGame());
//            }
//        }
    }

    private void advanceDay() {
        day++;
        dayOfWeek = dayOfWeek.next();

        if (day > 35) {
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
    public void updateTime(int hour,int day,DayOfWeek dayOfWeek,Season season){
        this.hour = hour;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.season = season;
    }
//    public String formattedTime() {
////        // Convert 24-hour format to 12-hour format with AM/PM
//        int displayHour = hour % 12 == 0 ? 12 : hour % 12;
//        String period = hour < 12 ? "AM" : "PM";
////
////        // Format as: "Tuesday, Spring 14 - 08:00 AM"
////        return String.format("%s, %s %02d - %02d:00 %s",
////            dayOfWeek.name(), season.name(), day, displayHour, period);
//        String dayStr = String.format("%02d", day);
//        String hourStr = String.format("%02d", displayHour);
//        String dowStr = (dayOfWeek != null) ? (dayOfWeek.name()) : "Unknown Day";
//        String seasonStr = (season != null) ? (season.name()) : "Unknown Season";
//
//        return dowStr + ", " + seasonStr + " " + dayStr + " - " + hourStr + ":00 " + period;
//
//    }
public String formattedTime() {
    int displayHour = hour % 12 == 0 ? 12 : hour % 12;
    String period = hour < 12 ? "AM" : "PM";

    String dayStr = String.format("%02d", day);
    String hourStr = String.format("%02d", displayHour);
    String dowStr = (dayOfWeek != null) ? (dayOfWeek.name()) : "Unknown Day";
    String seasonStr = (season != null) ? (season.name()) : "Unknown Season";

    String result = dowStr + ", " + seasonStr + " " + dayStr + " - " + hourStr + ":00 " + period;
    System.out.println("formattedTime: " + result); // ✅ Add this for debugging
    return result;
}


}

