package ru.minesweeper.view.highscore;

import javax.swing.*;
import java.awt.*;

public class WinFrame {
    private static final String FRAME_HEADER = "Победа";
    private static final String SCORE_LABEL_TEXT = "Количество очков :";
    private static final String TIME_LABEL_TEXT = "Время :";
    private static final String NAME_LABEL_TEXT = "Введите свое имя :";
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int tbColumnSize = 20;

    private JFrame mainFrame;
    private PlayerInfoSaver saver;
    private JButton saveButton;
    private JTextField tfName;
    private int score;
    private int timeSeconds;


    public WinFrame(PlayerInfoSaver saver, int score, int timeSeconds) {
        uiInit(score, timeSeconds);
        this.saver = saver;
    }

    public WinFrame(PlayerInfoSaver saver, int timeSeconds) {
        // для случаев когда подсчитывать очки бессмысленно
        uiInit(timeSeconds);
        this.saver = saver;
    }

    private void createFrame() {
        if (mainFrame == null) {
            mainFrame = new JFrame(FRAME_HEADER);
            mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
        }
    }

    private void uiInit(int score, int timeSeconds) {
        createFrame();
        mainFrame.add(createPanel(new JLabel(SCORE_LABEL_TEXT), new JLabel(String.valueOf(score))));
        this.score = score;
        uiInit(timeSeconds);
    }

    private void uiInit(int timeSeconds) {
        createFrame();
        createFloor(timeSeconds);
        this.timeSeconds = timeSeconds;
        frameSizeInit();
    }

    private void createFloor(int timeSeconds) {
        mainFrame.add(createPanel(new JLabel(TIME_LABEL_TEXT), new JLabel(String.valueOf(timeSeconds))));
        tfName = new JTextField();
        tfName.setColumns(tbColumnSize);
        mainFrame.add(createPanel(new JLabel(NAME_LABEL_TEXT), tfName));
        saveButton = new JButton("Сохранить");
        mainFrame.add(saveButton);

        saveButton.addActionListener( e -> {
                        saver.savePlayerInfo(tfName.getText(), score, timeSeconds);
                        mainFrame.dispose();
        });

        mainFrame.setVisible(true);
        mainFrame.setResizable(false);

        frameSizeInit();
    }

    private JPanel createPanel(JComponent left, JComponent right) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(left);
        panel.add(right);

        return panel;
    }

    private void frameSizeInit() {
        int w = screenSize.width;
        int h = screenSize.height;
        mainFrame.setBounds(w / 2, h / 2, w / 4, h / 4);
    }
}
