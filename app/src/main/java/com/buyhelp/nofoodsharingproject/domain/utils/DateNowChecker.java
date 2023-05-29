package com.buyhelp.nofoodsharingproject.domain.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateNowChecker {
    protected String timeHMS;
    protected String timeYMD;
    private ZonedDateTime dateTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DateNowChecker() {
        this.dateNowUpdate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void dateNowUpdate() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatterYMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.timeHMS = dateTime.format(formatterHMS);
        this.timeYMD = dateTime.format(formatterYMD);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getHour() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        return dateTime.getHour();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getMinute() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        return dateTime.getMinute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
