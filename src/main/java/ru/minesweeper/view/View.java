package ru.minesweeper.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.minesweeper.dto.Cell;
import ru.minesweeper.dto.Level;
import ru.minesweeper.eventdispatcher.finishgame.FinishGameListener;
import ru.minesweeper.eventdispatcher.finishgame.OnGameFinishDelegate;
import ru.minesweeper.eventdispatcher.flag.FlagListener;
import ru.minesweeper.eventdispatcher.flag.OnFlagDelegate;
import ru.minesweeper.eventdispatcher.open.OnOpenCellDelegate;
import ru.minesweeper.eventdispatcher.open.OpenCellsListener;
import ru.minesweeper.eventdispatcher.setlevel.SetLevelListener;
import ru.minesweeper.eventdispatcher.startgame.OnGameStartDelegate;
import ru.minesweeper.eventdispatcher.startgame.StartGameListener;
import ru.minesweeper.eventdispatcher.tick.GameTickListener;
import ru.minesweeper.eventdispatcher.win.WinListener;
import ru.minesweeper.view.field.FieldComponent;
import ru.minesweeper.view.field.MineCellButton;
import ru.minesweeper.view.highscore.PlayerInfoSaver;
import ru.minesweeper.view.highscore.WinFrame;
import ru.minesweeper.view.images.ImageType;
import ru.minesweeper.view.menu.HorisontalBar;
import ru.minesweeper.view.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class View implements OpenCellsListener, StartGameListener, FinishGameListener, GameTickListener, FlagListener, WinListener {
    private static final Logger log = LoggerFactory.getLogger(View.class);
    private Level level;
    private PlayerInfoSaver saver;
    private JFrame mainFrame;
    private Menu mainMenu;
    private FieldComponent field;
    private HorisontalBar horisontalBar;
    private static final int BUTTON_SIZE = 30;

    private final OnGameFinishDelegate<FinishGameListener> gameFinishDelegate = new OnGameFinishDelegate<>();
    private final OnGameStartDelegate<StartGameListener> gameStartDelegate = new OnGameStartDelegate<>();
    private final OnOpenCellDelegate<OpenCellsListener> openCellDelegate = new OnOpenCellDelegate<>();
    private final OnFlagDelegate<FlagListener> flagDelegate = new OnFlagDelegate<>();


    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


    public View() {
        log.info("Запуск конструктора");
        uiInit();
    }

    public void setPlayerInfoSaver(PlayerInfoSaver saver) {
        this.saver = saver;
    }

    private void uiInit() {
        log.info("инициализация UI");
        mainFrame = new JFrame("Сапер");
        mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));

        horisontalBar = new HorisontalBar();
        horisontalBar.bindClickListener(e -> {
            createField();
            gameFinishDelegate.broadcast();
            gameStartDelegate.broadcast(level);
            horisontalBar.clear();
        });
        mainFrame.add(horisontalBar);

        mainMenu = new Menu(mainFrame);
        mainFrame.setJMenuBar(mainMenu);

        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void bindFlagListener(FlagListener listener) {
        flagDelegate.bind(listener);
    }

    public void bindFinishGameListener(FinishGameListener listener) {
        gameFinishDelegate.bind(listener);
    }

    public void bindStartGameListener(StartGameListener listener) {
        gameStartDelegate.bind(listener);
    }

    public void bindOpenCellsListener(OpenCellsListener listener) {
        openCellDelegate.bind(listener);
    }
    public void bindSetLevel(SetLevelListener listener) {
       mainMenu.bindSetLevel(listener);
    }

    private void bindOnClickEvents() {
        for (MineCellButton button : field.getButtonArray()) {

            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    horisontalBar.setIconToIdle();
                    if (e.getButton() == MouseEvent.BUTTON1 && button.isEnableClickEvent()) {
                        Cell cell = button.getStruct();
                        log.info(String.format("Принял клик левой кнопкой по (%s, %s)", cell.x, cell.y));
                        openCellDelegate.broadcastOpenCell(button.getStruct());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (button.isEnableClickEvent()) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            horisontalBar.setIconToClick();
                            return;
                        }

                        if (button.isFlagged()) {
                            flagDelegate.broadcastRemove(button.getStruct());
                        } else {
                            flagDelegate.broadcastAdd(button.getStruct());
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    horisontalBar.setIconToIdle();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
        }
    }


    @Override
    public void executeOpenCells(List<Cell> cells) {
        log.info("Принял список открываемых ячеек");
        for (Cell cell : cells) {
            field.openValue(cell);
        }
    }

    @Override
    public void executeOpenCell(Cell cell) {
        // не понадобится. Открывает только список
    }


    @Override
    public void onGameFinishByBombs(List<Cell> bombs) {
        log.info("Принял список открываемых бомб");
        for (Cell cell : bombs) {
            field.openBomb(cell);
        }
        field.blockButtons();
        onGameFinish();
    }

    @Override
    public void onGameFinish() {
        horisontalBar.setButtonIcon(ImageType.EMOJI_DEAD);
        log.info("Вывожу окно конца игры");
        JOptionPane.showMessageDialog(mainFrame,
                "Конец игры",
                "Игра окончена",
                JOptionPane.INFORMATION_MESSAGE);
        gameFinishDelegate.broadcast();
    }

    @Override
    public void onGameStart() {
        log.info("Запуск новой игры");
        horisontalBar.setIconToIdle();
        horisontalBar.setTimeSeconds(0);
        createField();
    }

    @Override
    public void onGameStart(Level level) {
        this.level = level;
        onGameStart();
    }

    @Override
    public void onGameStart(Cell firstCell) {
        // не используется
    }

    private void createField() {
        if (field != null) {
            mainFrame.remove(field);
        }
        int cellCountX = level.sizeX;
        int cellCountY = level.sizeY;
        log.info(String.format("Генерация визуального поля %s x %s", cellCountX, cellCountY));
        int windowSizeX = screenSize.width / 2;
        int windowSizeY = screenSize.height / 2;
        int posX = (screenSize.width - windowSizeX) / 2;
        int posY = (screenSize.height - windowSizeY) / 2;
        Dimension mainFrameSize = calcSize();
        field = new FieldComponent(cellCountX, cellCountY, new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        mainFrame.add(field);
        bindOnClickEvents();
        mainFrame.setSize(1, 1); // Костыль, но свинг отказыватся перерисовывать без сворачивания или ресайза
        mainFrame.setBounds(posX, posY, mainFrameSize.width, mainFrameSize.height);


    }

    private Dimension calcSize() {

        int sizeX = level.sizeX * BUTTON_SIZE + BUTTON_SIZE; // + размер меню
        int sizeY = level.sizeY * BUTTON_SIZE + HorisontalBar.getPanelHeight(); // + расстояние по бокам
        return new Dimension(sizeX, sizeY);
    }

    @Override
    public void tickSecondsEvent(int seconds) {
        horisontalBar.setTimeSeconds(seconds);
    }

    @Override
    public void tickEvent() {

    }


    @Override
    public void addFlag(Cell flag) {
        field.addFlag(flag);
    }

    @Override
    public void removeFlag(Cell flag) {
        field.removeFlag(flag);

    }

    @Override
    public void updateFlagCount(int count) {
        horisontalBar.setFlagsCount(count);
    }

    @Override
    public void winGame(int score, int timeSeconds) {
        log.info("Открытие таблицы рекордов по очкам и времени");
        new WinFrame(saver, score, timeSeconds);
    }

    @Override
    public void winGame(int timeSeconds) {
        log.info("Открытие таблицы рекордов по времени");
        new WinFrame(saver, timeSeconds);
    }
}
