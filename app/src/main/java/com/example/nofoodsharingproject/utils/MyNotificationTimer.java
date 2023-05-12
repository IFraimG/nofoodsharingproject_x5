package com.example.nofoodsharingproject.utils;

import android.os.CountDownTimer;
import android.util.Log;

public class MyNotificationTimer extends CountDownTimer {
    private long timeElapsed;

    public MyNotificationTimer(long startTime, long interval) {
        super(startTime, interval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeElapsed = 1800000 - millisUntilFinished;
        Log.d("TIMER", "Time elapsed: " + timeElapsed);
    }

    @Override
    public void onFinish() {
        Log.d("TIMER", "Timer finished");
    }
}
