package ru.minesweeper.dto;

import java.util.Objects;

public class Cell {
    public int x;
    public int y;
    public String value;

    public Cell(int x, int y, String value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y && Objects.equals(value, cell.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, value);
    }
}
