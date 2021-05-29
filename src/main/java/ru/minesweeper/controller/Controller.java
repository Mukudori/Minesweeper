package ru.minesweeper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.minesweeper.dto.Cell;
import ru.minesweeper.dto.Level;
import ru.minesweeper.eventdispatcher.finishgame.FinishGameListener;
import ru.minesweeper.eventdispatcher.finishgame.OnGameFinishDelegate;
import ru.minesweeper.eventdispatcher.flag.FlagListener;
import ru.minesweeper.eventdispatcher.open.OnOpenCellDelegate;
import ru.minesweeper.eventdispatcher.open.OpenCellsListener;
import ru.minesweeper.eventdispatcher.setlevel.SetLevelListener;
import ru.minesweeper.eventdispatcher.startgame.OnGameStartDelegate;
import ru.minesweeper.eventdispatcher.startgame.StartGameListener;
import ru.minesweeper.eventdispatcher.tick.GameTickListener;
import ru.minesweeper.eventdispatcher.win.OnGameWin;
import ru.minesweeper.eventdispatcher.win.WinListener;
import ru.minesweeper.model.highscore.HighScoreTable;
import ru.minesweeper.model.highscore.PlayerInfo;
import ru.minesweeper.model.minesweeper.MinesweeperModel;
import ru.minesweeper.view.View;
import ru.minesweeper.view.highscore.PlayerInfoSaver;

import java.util.List;

public class Controller
        implements OpenCellsListener, StartGameListener, FinishGameListener, WinListener, PlayerInfoSaver, FlagListener, SetLevelListener {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private MinesweeperModel model;
    private View view;
    private PlayTimer timer = new PlayTimer();
    private HighScoreTable highScoreTable = new HighScoreTable();



    private OnOpenCellDelegate<OpenCellsListener> openCellDelegate = new OnOpenCellDelegate<>();
    private OnGameStartDelegate<StartGameListener> gameStartDelegate = new OnGameStartDelegate<>();
    private OnGameFinishDelegate<FinishGameListener> gameFinishDelegate = new OnGameFinishDelegate<>();
    private OnGameWin<WinListener> onGameWin = new OnGameWin<>();


    public Controller(MinesweeperModel model, View view) {
        log.info("Запуск конструктора");
        this.model = model;
        modelInit();
        this.view = view;
        viewInit();
        onGameStart(Level.NOVICE);
    }

    private void modelInit() {
        log.info("Запуск инициализации модели");
        model.bindOpenCellListener(this);
        model.bindFinishListener(this);
        model.bindWinGame(this);
        bindStartGame(model);
    }

    private void viewInit() {
        log.info("Запуск инициализации View");
        view.setPlayerInfoSaver(this);
        bindOpenCellListener(view);
        bindStartGame(view);
        bindFinishGame(view);
        bindFlagListener(view);
        bindWinListener(view);
        bindTickEvent(view);
        view.bindOpenCellsListener(this);
        view.bindStartGameListener(this);
        view.bindFinishGameListener(this);
        view.bindFlagListener(this);
        view.bindSetLevel(this);
    }

    @Override
    public void executeOpenCells(List<Cell> cells) {
        log.info("Рассылаю открытие списка ячеек");
        openCellDelegate.broadcastOpenCells(cells);
    }

    @Override
    public void executeOpenCell(Cell cell) {
        log.info(String.format("Принял клик по кнопке (%s , %s)", cell.x, cell.y));

        if (model.isGameStarted()) {
            model.openCell(cell);
        } else {
            log.info("Стартую игру для модели");
            model.onGameStart(cell);
        }
    }

    public void bindFinishGame(FinishGameListener listener) {
        model.bindFinishListener(listener);
        timer.bindFinishListener(listener);
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameStart(Level level) {
        log.info("Передача старта модели");
        gameStartDelegate.broadcast(level);
    }

    @Override
    public void onGameStart(Cell firstCell) {
        log.info("Старт игры");
        model.onGameStart(firstCell);
        if (!timer.isStarted()) {
            timer.start();
        }
    }

    public void bindTickEvent(GameTickListener listener) {
        timer.bindTickListener(listener);
    }

    public void bindOpenCellListener(OpenCellsListener listener) {
        openCellDelegate.bind(listener);
    }

    public void bindStartGame(StartGameListener listener) {
        gameStartDelegate.bind(listener);
    }

    @Override
    public void onGameFinishByBombs(List<Cell> bombs) {
        log.info("Рассылаю окончание по подрыву на бомбах");
        gameFinishDelegate.broadcast(bombs);
        onGameFinish();
    }

    @Override
    public void onGameFinish() {
            timer.stop();
    }

    public void bindFlagListener(FlagListener listener) {
        model.bindFlagListener(listener);
    }

    public void bindWinListener(WinListener listener) {
        onGameWin.bind(listener);
    }

    @Override
    public void winGame(int score, int timeSeconds) {
        // модель не знает о таймере
    }

    @Override
    public void winGame(int mineCount) {
        log.info("Пересылаю выйгрыш");
        if (timer.isStarted()) {
            timer.stop();
        }
        // нет условия потсчета очков, возвращаем просто количество мин
        onGameWin.broadcast(mineCount, timer.getTimeSeconds());

    }

    @Override
    public void savePlayerInfo(String name, int score, int timeSeconds) {
        highScoreTable.add(new PlayerInfo(name, score, timeSeconds));
    }

    @Override
    public void addFlag(Cell flag) {
        model.addFlag(flag);
    }

    @Override
    public void removeFlag(Cell flag) {
        model.removeFlag(flag);
    }

    @Override
    public void updateFlagCount(int count) {

    }

    @Override
    public void onSetLevel(Level level) {
        model.onGameStart(level);
        onGameStart(level);
    }
}
