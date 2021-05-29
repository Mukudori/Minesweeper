package ru.minesweeper.eventdispatcher.startgame;

import ru.minesweeper.dto.Cell;
import ru.minesweeper.dto.Level;

public interface StartGameListener {
    void onGameStart();
    void onGameStart(Level level);
    void onGameStart(Cell firstCell);
}
