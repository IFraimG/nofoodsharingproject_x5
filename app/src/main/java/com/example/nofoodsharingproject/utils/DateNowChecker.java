package com.example.nofoodsharingproject.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateNowChecker {
    String timeHMS;
    String timeYMD;
    ZonedDateTime dateTime;

    public DateNowChecker() {
        this.dateNowUpdate();
    }

    public void dateNowUpdate() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatterYMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.timeHMS = dateTime.format(formatterHMS);
        this.timeYMD = dateTime.format(formatterYMD);
    }

    public int getHour() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        return dateTime.getHour();
    }

    public int getMinute() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        return dateTime.getMinute();
    }

    public int getSecond() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        return dateTime.getSecond();
    }

    public boolean isNight() {
        return false;
    }


    public String getTimeHMS() {
        return timeHMS;
    }

    public String getTimeYMD() {
        return timeYMD;
    }

    public void setTimeHMS(String timeHMS) {
        this.timeHMS = timeHMS;
    }

    public void setTimeYMD(String timeYMD) {
        this.timeYMD = timeYMD;
    }
}
