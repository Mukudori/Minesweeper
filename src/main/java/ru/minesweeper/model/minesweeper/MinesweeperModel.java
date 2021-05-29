package ru.minesweeper.model.minesweeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.minesweeper.dto.Cell;
import ru.minesweeper.dto.CellMark;
import ru.minesweeper.dto.Level;
import ru.minesweeper.eventdispatcher.finishgame.FinishGameListener;
import ru.minesweeper.eventdispatcher.finishgame.OnGameFinishDelegate;
import ru.minesweeper.eventdispatcher.flag.FlagListener;
import ru.minesweeper.eventdispatcher.flag.OnFlagDelegate;
import ru.minesweeper.eventdispatcher.open.OnOpenCellDelegate;
import ru.minesweeper.eventdispatcher.open.OpenCellsListener;
import ru.minesweeper.eventdispatcher.startgame.StartGameListener;
import ru.minesweeper.eventdispatcher.win.OnGameWin;
import ru.minesweeper.eventdispatcher.win.WinListener;

import java.util.*;

public class MinesweeperModel implements StartGameListener {
    private static final Logger log = LoggerFactory.getLogger(MinesweeperModel.class);
    private final OnOpenCellDelegate<OpenCellsListener> openCellDelegate = new OnOpenCellDelegate<>();
    private final OnGameFinishDelegate<FinishGameListener> gameFinishDelegate = new OnGameFinishDelegate<>();
    private final OnFlagDelegate<FlagListener> onFlagDelegate = new OnFlagDelegate<>();
    private final OnGameWin<WinListener> onGameWin = new OnGameWin<>();
    private final Random rand = new Random();
    private Level level;
    private List<Cell> bombs; // Для открытия бомб, если игрок "подорвался"
    private List<Cell> flags;
    private List<List<Boolean>> cellVisited; // Для проверки пустых ячеек
    private Set<Cell> cellsHasOpenned;
    private Set<Cell> openList;    // Для отправки ячеек на открытие


    private List<List<Integer>> field; // основное поле модели
    private boolean gameStarted;

    public MinesweeperModel() {
        this(Level.NOVICE);
    }

    public MinesweeperModel(Level level) {
        log.info("Запуск конструктора");
        this.level = level;
    }

    public int getSizeX() {
        return level.sizeX;
    }

    public int getSizeY() {
        return level.sizeY;
    }

    private void checkWin() {
        log.info(String.format( "openned %s, expected %s",cellsHasOpenned.size(),  level.sizeX * level.sizeY - bombs.size()));
        if (cellsHasOpenned.size() == level.sizeX * level.sizeY - bombs.size()) {
            int score = 0;
            for (Cell flag : flags) {
                if (checkBomb(flag)) {
                    score++;
                }
            }
            log.info("Сообщаю о выйгрыше");
            onGameWin.broadcast(score);
            gameStarted = false;
            return;
        }

        log.info("Продолжаем игру");
    }

    public void openCell(Cell cell) {
        log.info(String.format("Проверка %s %s", cell.x, cell.y));
        if (checkBomb(cell.x, cell.y)) {
            log.info("Это бомба");
            gameFinishDelegate.broadcast(bombs);
            gameStarted = false;
        } else {
            log.info("Ячейка безопасна");
            openCellDelegate.broadcastOpenCells(getOpenedCellsList(cell.x, cell.y));
            checkWin();
        }

    }

    public void bindOpenCellListener(OpenCellsListener listener) {
        openCellDelegate.bind(listener);
    }

    public void bindFinishListener(FinishGameListener listener) {
        gameFinishDelegate.bind(listener);
    }

    private boolean checkBomb(Cell cell) {
        return checkBomb(cell.x, cell.y);
    }

    private boolean checkBomb(int x, int y) {
        return field.get(y).get(x) == CellMark.BOMB.intVal;
    }

    boolean isOutRange(int x, int y) {
        return x < 0 || y < 0 || x >= getSizeX() || y >= getSizeY();
    }

    private void reveal(int x, int y) {
        if (isOutRange(x, y)) {
            return;
        }
        if (cellVisited.get(y).get(x)) {
            return;
        }
        cellVisited.get(y).set(x, true);
        log.info(String.format("Добавляю в список открытых (%s, %s)", x, y));
        openList.add(getCell(x, y));
        cellsHasOpenned.add(new Cell(x, y, String.valueOf(getCellValue(x, y))));

        if (getCellValue(x, y) != 0) {
            return;
        }

        reveal(x - 1, y - 1);
        reveal(x - 1, y + 1);
        reveal(x + 1, y - 1);
        reveal(x + 1, y + 1);
        reveal(x - 1, y);
        reveal(x + 1, y);
        reveal(x, y - 1);
        reveal(x, y + 1);
    }

    private List<Cell> getOpenedCellsList(int x, int y) {

        // пересоздаем флажки и возвращаемые ячейки
        if (openList != null) {
            openList.clear();
        }
        openList = new HashSet<>();
        Cell cell = new Cell(x, y, String.valueOf(getCellValue(x, y)));

        if (cellVisited != null) {
            cellVisited.clear();
        }
        cellVisited = new ArrayList<>(getSizeY());
        for (int i = 0; i < getSizeY(); i++) {
            List<Boolean> line = new ArrayList<>(getSizeX());
            for (int j = 0; j < getSizeX(); j++) {
                line.add(false);
            }
            cellVisited.add(line);
        }

        // рекурсивно обходим пустые ячейки до ближайших не пустых
        reveal(cell.x, cell.y);

        return new ArrayList<>(openList);
    }

    private void createField(int sizeX, int sizeY) {
        if (field != null) {
            field.clear();
        }
        field = new ArrayList<>(sizeY);
        for (int i = 0; i < sizeY; i++) {
            List<Integer> line = new ArrayList<>(sizeX);
            for (int j = 0; j < sizeX; j++) {
                line.add(0);
            }
            field.add(line);
        }

        if (bombs != null) {
            bombs.clear();
        }

        bombs = new ArrayList<>(level.mineCount);

        if (flags != null) {
            flags.clear();
        }

        flags = new ArrayList<>(level.mineCount);

        if (cellsHasOpenned != null) {
            cellsHasOpenned.clear();
        }

        cellsHasOpenned = new HashSet<>(sizeX*sizeY);

        log.info(String.format("Создано поле размером %s x %s ", getSizeX(), getSizeY()));
    }

    private Cell getCell(int x, int y) {
        return new Cell(x, y, String.valueOf(getCellValue(x, y)));
    }

    private int getCellValue(int x, int y) {
        return field.get(y).get(x);
    }

    private int incrementAndGet(int x, int y) {
        int var = getCellValue(x,y);
        field.get(y).set(x, ++var);
        return var;
    }

    private void putMine(int x, int y) {
        field.get(y).set(x, CellMark.BOMB.intVal);
        bombs.add(new Cell(x, y, CellMark.BOMB.strVal));
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int col = i + x;
                int row = j + y;
                if (col >= 0 && col < getSizeX() // вертикальные границы
                        && row >= 0 && row < getSizeY() // горизонтальные границы
                        && getCellValue(col, row) > CellMark.BOMB.intVal) { // не мина
                    incrementAndGet(col, row);
                }

            }
        }

    }

    private boolean mineContains(int x, int y) {
        for (Cell cell : bombs) {
            if (cell.x == x && cell.y == y)
                return true;
        }
        return false;
    }


    private void generateField(int x, int y) {
        createField(level.sizeX, level.sizeY);

        for (int i = 0; i < level.mineCount; i++) {
            while (true) {
                int by = rand.nextInt(level.sizeY - 1);
                int bx = rand.nextInt(level.sizeX - 1);
                // рандомим пока не наткнемся на нужные координаты
                if (!mineContains(bx, by) && !(x == bx && y == by)) {
                    putMine(bx, by);
                    break;
                }
            }

        }
        log.info(String.format("Сгенерировано %s мин", level.mineCount));
    }

    @Override
    public void onGameStart() {
        log.info("Старт игры без параметров");
        onGameStart(new Cell(0, 0, CellMark.EMPTY.strVal));
    }

    @Override
    public void onGameStart(Level level) {
        log.info("Старт игры по уровню " + level.levelName);
        this.level = level;
        gameStarted = false;
    }

    @Override
    public void onGameStart(Cell firstCell) {
        log.info(String.format("Старт игры по ячейке %s %s", firstCell.x, firstCell.y));
        generateField(firstCell.x, firstCell.y);
        openCell(firstCell);
        gameStarted = true;
        onFlagDelegate.broadcastUpdate(level.mineCount);
        //debugPrint();
    }

    /*private void debugPrint(){
        for (int i=0; i<field.size(); i++) {
            String line = "";
            for (int j = 0; j < field.get(0).size();  j++) {
                line += " " + getCellValue(j,i);
            }
            System.out.println(line);
        }
    }*/

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void bindFlagListener(FlagListener listener) {
        onFlagDelegate.bind(listener);
    }

    public void bindWinGame(WinListener listener) {
        onGameWin.bind(listener);
    }


    public void addFlag(Cell flag) {
        int availableCount = level.mineCount - flags.size() -1;
        if (0 <= availableCount) {
            flags.add(flag);
            onFlagDelegate.broadcastAdd(flag);
            onFlagDelegate.broadcastUpdate(availableCount);
        }
    }

    public void removeFlag(Cell flag) {
        int availableCount = level.mineCount - flags.size() + 1;
        for (int i=0; i<flags.size(); i++) {
            int x = flags.get(i).x;
            int y = flags.get(i).y;
            if (flag.x == x && flag.y == y) {
                flags.remove(i);
                onFlagDelegate.broadcastRemove(flag);
                onFlagDelegate.broadcastUpdate(availableCount);
                return;
            }
        }
    }

}




