package ru.minesweeper.eventdispatcher.setlevel;

import ru.minesweeper.dto.Level;
import ru.minesweeper.eventdispatcher.EventDispatcher;

public class OnSetLevelDelegate<T extends SetLevelListener> extends EventDispatcher<T> {
    @Override
    public void broadcast() {
    }

    public void broadcastNewLevel(Level level){
        for (SetLevelListener listener : listenersList) {
            listener.onSetLevel(level);
        }
    }
}
