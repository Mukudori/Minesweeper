package ru.minesweeper.eventdispatcher.win;

public interface WinListener {
    void winGame(int score, int timeSeconds);
    void winGame(int timeSeconds);
}
