package ru.minesweeper.eventdispatcher.flag;

import ru.minesweeper.dto.Cell;

public interface FlagListener {
    void addFlag(Cell flag);
    void removeFlag(Cell flag);
    void updateFlagCount(int count);
}
