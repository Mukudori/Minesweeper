package ru.minesweeper.view.menu;

import javax.swing.*;

public class HelpView {
    private static final String HEADER = "О программе";
    private static final String CONTENT = "Игра работает по стандартным правилам Сапера. "+ System.lineSeparator() +
            "Начисление очков происходит по правильно отмечаным флажкам." + System.lineSeparator().repeat(2) +
            "Разработал: Шпаков В.К.";

   public HelpView(JFrame parentFrame) {
       JOptionPane.showMessageDialog(parentFrame,
               CONTENT,
               HEADER,
               JOptionPane.INFORMATION_MESSAGE);
   }
}
