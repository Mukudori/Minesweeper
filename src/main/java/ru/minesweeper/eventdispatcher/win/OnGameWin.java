package ru.minesweeper.eventdispatcher.win;

import ru.minesweeper.eventdispatcher.EventDispatcher;

public class OnGameWin<T extends WinListener> extends EventDispatcher<T> {
    @Override
    public void broadcast() {
    }

    public void broadcast(int score, int timeSeconds) {
        for (WinListener listener : listenersList) {
            listener.winGame(score,timeSeconds);
        }
    }

    public void broadcast(int score) {
        for (WinListener listener : listenersList) {
            listener.winGame(score);
        }
    }
}
