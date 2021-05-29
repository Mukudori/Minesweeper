package ru.minesweeper.eventdispatcher.startgame;

import ru.minesweeper.dto.Level;
import ru.minesweeper.eventdispatcher.EventDispatcher;

public class OnGameStartDelegate <T extends StartGameListener> extends EventDispatcher<T> {
    @Override
    public void broadcast() {
        for (StartGameListener listener : listenersList) {
            listener.onGameStart();
        }
    }


    public void broadcast(Level level) {
        for (StartGameListener listener : listenersList) {
            listener.onGameStart(level);
        }
    }
}
