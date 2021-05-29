package ru.minesweeper.controller;

import ru.minesweeper.eventdispatcher.finishgame.FinishGameListener;
import ru.minesweeper.eventdispatcher.finishgame.OnGameFinishDelegate;
import ru.minesweeper.eventdispatcher.tick.GameTickListener;
import ru.minesweeper.eventdispatcher.tick.OnGameTickDelegate;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PlayTimer {
    private final Timer swingTimer;
    private final int secondsLimit;
    private int seconds;
    private final OnGameFinishDelegate<FinishGameListener> finishGameDelegate;
    private final OnGameTickDelegate<GameTickListener> tickDelegate;
    private boolean started;

    public PlayTimer() {
        this(999);
    }

    public PlayTimer(int secondsLimit) {
        this.secondsLimit = secondsLimit;
        finishGameDelegate = new OnGameFinishDelegate<>();
        tickDelegate = new OnGameTickDelegate<>();
        swingTimer = new Timer(1000, this.createListener());
    }

    public void start() {
        seconds = 0;
        swingTimer.start();
        started = true;
    }

    public void bindFinishListener(FinishGameListener listener) {
        finishGameDelegate.bind(listener);
    }

    public void bindTickListener(GameTickListener listener) {
        tickDelegate.bind(listener);
    }

    private ActionListener createListener() {
        return e -> {
            if (seconds < secondsLimit) {
                seconds++;
                tickDelegate.broadcast(seconds);
            } else {
                finishGameDelegate.broadcast();
                swingTimer.stop();
            }
        };
    }

    public boolean isStarted() {
        return started;
    }

    public void stop() {
        swingTimer.stop();
        started = false;
    }

    public int getTimeSeconds() {
        return seconds;
    }


}
