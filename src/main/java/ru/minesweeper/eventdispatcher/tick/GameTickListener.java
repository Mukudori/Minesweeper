package ru.minesweeper.eventdispatcher.tick;

public interface GameTickListener {
    void tickSecondsEvent(int seconds);
    void tickEvent();
}
