package com.buyhelp.nofoodsharingproject.domain.utils;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class DateNowCheckerOld extends DateNowChecker {
    private ZonedDateTime dateTime;

    public DateNowCheckerOld() {
        this.dateNowUpdate();
    }

    @Override
    public void dateNowUpdate() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter formatterYMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.timeHMS = dateTime.format(formatterHMS);
        this.timeYMD = dateTime.format(formatterYMD);
    }

    @Override
    public int getHour() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        return dateTime.getHour();
    }
    @Override
    public int getMinute() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        return dateTime.getMinute();
    }

    @Override
    public int getSecond() {
        this.dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));

        return dateTime.getSecond();
    }

    @Override
    public boolean isNight() {
        return false;
    }

    @Override
    public String getTimeHMS() {
        return this.timeHMS;
    }

    @Override
    public String getTimeYMD() {
        return this.timeYMD;
    }

    @Override
    public void setTimeHMS(String timeHMS) {
        this.timeHMS = timeHMS;
    }

    @Override
    public void setTimeYMD(String timeYMD) {
        this.timeYMD = timeYMD;
    }
}
