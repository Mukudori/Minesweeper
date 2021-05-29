package ru.minesweeper.eventdispatcher.open;

import ru.minesweeper.dto.Cell;
import ru.minesweeper.eventdispatcher.EventDispatcher;

import java.util.List;

public class OnOpenCellDelegate <T extends OpenCellsListener> extends EventDispatcher<T> {
    public void broadcastOpenCells(List<Cell> cells) {
        for (OpenCellsListener listener : listenersList) {
            listener.executeOpenCells(cells);
        }
    }

    public void broadcastOpenCell(Cell cell){
        for (OpenCellsListener listener : listenersList) {
            listener.executeOpenCell(cell);
        }
    }

    @Override
    public void broadcast() {
        // пока нет ситуаций, где пригодится
    }
}
