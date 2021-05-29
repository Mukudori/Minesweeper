package ru.minesweeper.dto;

public enum Level {
    NOVICE (9,9,10, "Новичек"),
    AMATEUR (16,16,40, "Любитель"),
    PROFESSIONAL(30,16,99, "Профессионал");

    public final int sizeX;
    public final int sizeY;
    public final int mineCount;
    public final String levelName;

    Level(int sizeX, int sizeY, int mineCount, String name) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.mineCount = mineCount;
        this.levelName = name;
    }



}
