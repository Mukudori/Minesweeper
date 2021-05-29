package ru.minesweeper.eventdispatcher.tick;

import ru.minesweeper.eventdispatcher.EventDispatcher;

public class OnGameTickDelegate<T extends GameTickListener> extends EventDispatcher<T> {

    public void broadcast(int seconds){
        for (GameTickListener listener : listenersList) {
            listener.tickSecondsEvent(seconds);
        }
    }

    @Override
    public void broadcast() {
        for (GameTickListener listener : listenersList) {
            listener.tickEvent();
        }
    }
}
