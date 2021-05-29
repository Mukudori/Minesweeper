package ru.minesweeper.eventdispatcher;

import java.util.ArrayList;
import java.util.List;

public abstract class EventDispatcher <T> {
    protected List<T> listenersList = new ArrayList<>();

    public void bind(T listener) {
        listenersList.add(listener);
    }

    public void unbind(T listener) {
        listenersList.remove(listener);
    }

    public abstract void broadcast();


}
