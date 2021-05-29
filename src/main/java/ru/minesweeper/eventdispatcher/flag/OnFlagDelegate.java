package ru.minesweeper.eventdispatcher.flag;

import ru.minesweeper.dto.Cell;
import ru.minesweeper.eventdispatcher.EventDispatcher;

public class OnFlagDelegate<T extends FlagListener> extends EventDispatcher<T> {
    @Override
    public void broadcast() {
    }

    public void broadcastAdd(Cell flag) {
        for (FlagListener listener : listenersList) {
            listener.addFlag(flag);
        }
    }

    public void broadcastRemove(Cell flag) {
        for (FlagListener listener : listenersList) {
            listener.removeFlag(flag);
        }
    }

    public void broadcastUpdate(int count) {
        for (FlagListener listener : listenersList) {
            listener.updateFlagCount(count);
        }
    }
}
