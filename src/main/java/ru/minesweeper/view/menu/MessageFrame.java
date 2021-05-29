package ru.minesweeper.view.menu;

import javax.swing.*;
import java.awt.*;

public class MessageFrame {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public MessageFrame(String title, String message) {
        JFrame mainFrame = new JFrame(title);
        mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
        mainFrame.add(new JLabel(message));
        JButton button = new JButton("ок");
        mainFrame.add(button);
        button.addActionListener(e-> mainFrame.dispose());
        mainFrame.setBounds(screenSize.height /4, screenSize.width/4, screenSize.width/2, screenSize.height/2);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
