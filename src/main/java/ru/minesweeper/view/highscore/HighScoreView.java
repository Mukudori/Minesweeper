package ru.minesweeper.view.highscore;

import ru.minesweeper.model.highscore.PlayerInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HighScoreView {
    private static final String HEADER = "Таблица рекордов";
    private JFrame mainFrame;


    private JPanel contentPanel;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension PREFERRED_SCROLLPANE_DIMENSION = new Dimension( 800,300);
    private static final String COLUMN_1_HEADER = " № ";
    private static final String COLUMN_2_HEADER = " Имя игрока ";
    private static final String COLUMN_3_HEADER = " Количество очков ";
    private static final String COLUMN_4_HEADER = " Время ";

    public HighScoreView(List<PlayerInfo> playerInfoList) {
        uiInit(playerInfoList);
    }

    public void uiInit(List<PlayerInfo> playerInfoList) {
        mainFrame = new JFrame(HEADER);
        contentPanel = new JPanel();
        GridBagLayout grid = new GridBagLayout();


        mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));
        contentPanel.setLayout(grid);
        createRow(0,COLUMN_2_HEADER, COLUMN_3_HEADER, COLUMN_4_HEADER);

        for (int i=0; i<playerInfoList.size(); i++) {
            PlayerInfo pInf = playerInfoList.get(i);
            createRow(i+1, pInf.name, String.valueOf(pInf.score), String.valueOf(pInf.time));
        }



        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(PREFERRED_SCROLLPANE_DIMENSION);
        mainFrame.add(scrollPane);

        mainFrame.setVisible(true);
        int w = SCREEN_SIZE.width;
        int h = SCREEN_SIZE.height;
        mainFrame.setBounds(w / 2, h / 2, w / 4, h / 4);

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private void createRow(int row, String name, String score, String timeSeconds) {
        JLabel lName = new JLabel(name);
        JLabel lScore = new JLabel(String.valueOf(score));
        JLabel lTime = new JLabel(String.valueOf(timeSeconds));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.FIRST_LINE_START;
        c.gridy = row;
        c.gridx = 0;
        if (row == 0) {
            contentPanel.add(new JLabel(COLUMN_1_HEADER), c);
        } else {
            contentPanel.add(new JLabel(String.valueOf(row)), c);
        }
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1;
        contentPanel.add(lName, c);
        c.gridx = 2;
        contentPanel.add(lScore, c);
        c.gridx = 3;
        contentPanel.add(lTime, c);
    }
}
