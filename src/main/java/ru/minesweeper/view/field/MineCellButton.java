package ru.minesweeper.view.field;

import ru.minesweeper.dto.Cell;
import ru.minesweeper.view.images.ImagePool;
import ru.minesweeper.view.images.ImageType;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class MineCellButton extends JButton {
    private boolean flagged;
    private Cell cellStruct;
    private boolean enableClickEvent;



    public void setEnabledClickEvent(boolean isEnabled) {
        enableClickEvent = isEnabled;
        setSelected(!isEnabled);
    }


    public MineCellButton(int x, int y, Dimension size) {
        setText("");
        cellStruct = new Cell(x, y, "");
        setPreferredSize(size);
        enableClickEvent = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void openBomb() {
        setIcon(ImagePool.get(ImageType.BOMB));
        setBackground(Color.RED);
        setEnabledClickEvent(false);
        setText("");
    }

    public void openValue(String value) {
        if (!enableClickEvent) {
            return;
        }

        if (!flagged) {
            Optional<ImageIcon> imageIcon = ImagePool.get(value);
            imageIcon.ifPresent(this::setIcon);
            cellStruct.value = value;
            setEnabledClickEvent(false);
        }
    }

    public Cell getStruct() {
        return cellStruct;
    }

    public boolean isEnableClickEvent() {
        return enableClickEvent;
    }

    public void addFlag() {
        setIcon(ImagePool.get(ImageType.FLAG));
        flagged = true;
    }

    public void removeFlag() {
        setIcon(new ImageIcon());
        flagged = false;
    }
}
