package ru.minesweeper.dto;

public enum CellMark {
    BOMB(-1, "x"),
    EMPTY(0, "0");

    public final int intVal;
    public final String strVal;

    CellMark(int intVal, String strVal) {
        this.intVal = intVal;
        this.strVal = strVal;
    }
}
