package ru.minesweeper.view.menu;

import ru.minesweeper.view.images.ImagePool;
import ru.minesweeper.view.images.ImageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HorisontalBar extends JPanel {
    private static final int FONT_SIZE = 20;
    private static final int DIGIT_SIZE = 3;
    private static final int BUTTON_SIZE = 30;
    private static final int PANEL_HEIGHT = 110;

    private JLabel flagsLabel;
    private JButton resetButton;
    private JLabel timeLabel;



    public HorisontalBar() {
        uiInit();
        setFlagsCount(0);
        setTimeSeconds(0);
    }

    private void uiInit() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        flagsLabel = new JLabel();
        setFont(flagsLabel);
        add(flagsLabel);

        resetButton = new JButton();
        setIconToIdle();
        resetButton.setPreferredSize(new Dimension(BUTTON_SIZE,BUTTON_SIZE));
        add(resetButton);

        timeLabel = new JLabel();
        setFont(timeLabel);
        add(timeLabel);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setVisible(true);

    }

    public void setTimeSeconds(int seconds) {
        timeLabel.setText(formatInt(seconds));
    }

    public void setFlagsCount(int count) {
        flagsLabel.setText(formatInt(count));
    }

    public void setButtonIcon(ImageType type) {
            resetButton.setIcon(ImagePool.get(type));
    }

    public void setIconToClick() {
        setButtonIcon(ImageType.EMOJI_CLICK);
    }

    public void setIconToIdle() {
        setButtonIcon(ImageType.EMOJI_IDLE);
    }



    public void bindClickListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

    private String formatInt(int value) {
        String num = String.valueOf(value);

        if (num.length() < DIGIT_SIZE) {
            return "0".repeat(DIGIT_SIZE - num.length()) + num;
        }

        return num;
    }

    private void setFont(JLabel label) {
        Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, FONT_SIZE));
    }

    public static int getPanelHeight() {
        return PANEL_HEIGHT;
    }

    public void clear() {
        setFlagsCount(0);
        setTimeSeconds(0);
    }
}
