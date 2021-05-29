package ru.minesweeper.model.highscore;

import java.io.Serializable;

public class PlayerInfo implements Serializable{
    // Воспринимаю объект как структуру без логики
    // поэтому гэттеры не нужны
    public final String name;
    public final Integer score;
    public final Integer time;

    public PlayerInfo(String name, int score, int time) {
        super();
        this.name = name;
        this.score = score;
        this.time = time;
    }
}
