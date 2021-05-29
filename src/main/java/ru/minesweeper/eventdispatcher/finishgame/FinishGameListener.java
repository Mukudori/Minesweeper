package ru.minesweeper.eventdispatcher.finishgame;

import ru.minesweeper.dto.Cell;

import java.util.List;

public interface FinishGameListener {
    void onGameFinishByBombs(List<Cell> bombs);
    void onGameFinish();
}
