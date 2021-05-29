package ru.minesweeper.eventdispatcher.open;

import ru.minesweeper.dto.Cell;

import java.util.List;

public interface OpenCellsListener {
    void executeOpenCells(List<Cell> cells);
    void executeOpenCell(Cell cell);

}

