package ru.minesweeper;

import ru.minesweeper.controller.Controller;
import ru.minesweeper.dto.Level;
import ru.minesweeper.model.minesweeper.MinesweeperModel;
import ru.minesweeper.view.View;

public class App {

    public static void main(String[] args) {
        Controller controller = new Controller(new MinesweeperModel(Level.NOVICE), new View());

    }
}
