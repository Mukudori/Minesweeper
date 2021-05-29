package ru.minesweeper.eventdispatcher.setlevel;

import ru.minesweeper.dto.Level;

public interface SetLevelListener {
    void onSetLevel(Level level);
}
