/**
 * Класс {@code DateNowCheckerOld} необходим для работы с датами
 * Функции этого класса доступны только для Android, версии НИЖЕ 8.0
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.domain.utils;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class DateNowCheckerOld extends DateNowChecker {
    private ZonedDateTime dateTime;

    public DateNowCheckerOld() {
        this.dateNowUpdate();
    }

    /**
     * Метод для форматирования даты в форматы Hours:Minutes:Seconds и Years:Months:Days
     * Локализация времени происходит по Московскому времени
     */
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
}
