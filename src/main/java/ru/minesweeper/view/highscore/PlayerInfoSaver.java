package ru.minesweeper.view.highscore;

public interface PlayerInfoSaver {
    void savePlayerInfo(String name, int score, int timeSeconds);
}
