package ru.minesweeper.eventdispatcher.finishgame;

import ru.minesweeper.dto.Cell;
import ru.minesweeper.eventdispatcher.EventDispatcher;

import java.util.List;

public class OnGameFinishDelegate <T extends FinishGameListener> extends EventDispatcher<T> {
    public void broadcast(List<Cell> bombs) {
       for (FinishGameListener listener : listenersList) {
           listener.onGameFinishByBombs(bombs);
        }
    }

    @Override
    public void broadcast() {
        for (FinishGameListener listener : listenersList) {
            listener.onGameFinish();
        }
    }
}
