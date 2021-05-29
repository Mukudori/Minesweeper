package ru.minesweeper.view.menu;

import ru.minesweeper.dto.Level;
import ru.minesweeper.eventdispatcher.setlevel.OnSetLevelDelegate;
import ru.minesweeper.eventdispatcher.setlevel.SetLevelListener;
import ru.minesweeper.model.highscore.HighScoreTable;
import ru.minesweeper.view.highscore.HighScoreView;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Menu extends JMenuBar {
    private final JFrame parentFrame;
    private final OnSetLevelDelegate<SetLevelListener> setLevelDelegate = new OnSetLevelDelegate<>();

    public Menu(JFrame parent) {
        parentFrame = parent;
        add(createGameMenu());
        add(createHelpMenu());
    }

    private JMenu createGameMenu() {
        JMenu game = new JMenu("Игра");
        JMenuItem newGame = new JMenuItem("Новая игра");
        game.add(newGame);

        JMenuItem novice = new JMenuItem("Новичек");
        game.add(novice);
        novice.addActionListener(e -> restartGame(Level.NOVICE));

        JMenuItem amateur = new JMenuItem("Любитель");
        game.add(amateur);
        amateur.addActionListener(e -> restartGame(Level.AMATEUR));

        JMenuItem pro = new JMenuItem("Профессионал");
        game.add(pro);
        pro.addActionListener(e -> restartGame(Level.PROFESSIONAL));

        JMenuItem highScores = new JMenuItem("Таблица рекордов");
        game.add(highScores);
        highScores.addActionListener(e -> new HighScoreView(new HighScoreTable().getTable()));

        JMenuItem exit = new JMenuItem("Выход");
        exit.addActionListener(createExitEvent());
        game.add(exit);

        return game;
    }

    private JMenu createHelpMenu() {
        JMenu help = new JMenu("Справка");
        JMenuItem about = new JMenuItem("О программе");
        about.addActionListener(e -> new HelpView(parentFrame));

        help.add(about);
        return help;
    }


    private ActionListener createExitEvent() {
        return e -> parentFrame.dispose();
    }

    private void restartGame(Level level) {
        setLevelDelegate.broadcastNewLevel(level);
    }

    public void bindSetLevel(SetLevelListener listener) {
        setLevelDelegate.bind(listener);
    }


}
