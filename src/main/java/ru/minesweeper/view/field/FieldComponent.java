package ru.minesweeper.view.field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.minesweeper.dto.Cell;
import ru.minesweeper.eventdispatcher.finishgame.FinishGameListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FieldComponent extends JComponent implements FinishGameListener {
    private int columnCount;
    private int rowCount;
    private List<List<MineCellButton>> buttonArray;
    private final Dimension buttonSize;
    private static final Logger log = LoggerFactory.getLogger(FieldComponent.class);

    public FieldComponent(int sizeX, int sizeY, Dimension buttonSize) throws HeadlessException {
        log.info("Запуск конструктора");
        this.buttonSize = buttonSize;
        this.columnCount = sizeX;
        this.rowCount = sizeY;
        initUI();
    }

    public MineCellButton get(Cell cell) {
        return get(cell.x, cell.y);
    }


    public MineCellButton get(int x, int y) {
        return buttonArray.get(y).get(x);
    }

    private void initUI() {
        log.info(String.format("Создание поля %s x %s", rowCount, columnCount));
        buttonArray = new ArrayList<>(rowCount);
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        setLayout(grid);
        for (int y = 0; y < rowCount; y++) {
            List<MineCellButton> line = new ArrayList<>(columnCount);
            for (int x = 0; x < columnCount; x++) {
                c.gridx = x;
                c.gridy = y;
                MineCellButton button = new MineCellButton(x,y, buttonSize);
                add(button, c);
                line.add(button);
            }
            buttonArray.add(line);
        }
        setVisible(true);
    }

    public List<MineCellButton> getButtonArray() {
        List<MineCellButton> result = new ArrayList<>(columnCount*rowCount);
        for (List<MineCellButton> line : buttonArray) {
            result.addAll(line);
        }
        return result;
    }

    public void openValue(Cell cell) {
        get(cell).openValue(cell.value);

    }

    public void openBomb(Cell cell) {
        MineCellButton button = get(cell);
        if (!button.isFlagged()) {
            button.openBomb();
        }
    }


    @Override
    public void onGameFinishByBombs(List<Cell> bombs) {
        for (Cell cell : bombs) {
           get(cell).openBomb();
        }
    }

    @Override
    public void onGameFinish() {
        // Модель не может завершать игру без причины
    }

    public void blockButtons() {
        for (MineCellButton button : getButtonArray()) {
            button.setEnabledClickEvent(false);
        }
    }

    public void addFlag(Cell flag) {
        get(flag).addFlag();
    }

    public void removeFlag(Cell flag) {
        get(flag).removeFlag();
    }
}
