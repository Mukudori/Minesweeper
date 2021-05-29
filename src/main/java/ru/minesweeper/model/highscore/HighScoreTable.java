package ru.minesweeper.model.highscore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HighScoreTable {
    private static final String FILE_NAME = "score_table.dat";

    private List<PlayerInfo> table = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(HighScoreTable.class);

    public HighScoreTable() {
        load();
    }


    private void load() {
        log.info("Загрузка таблицы из файла " + FILE_NAME);
        File f = new File(FILE_NAME);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                table = (List<PlayerInfo>) ois.readObject();
                log.info("");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        } else {
            log.warn("Сохранненая таблица рекордов отсутствует");
        }

    }

    private void save() {
        if (!table.isEmpty()) {
            log.info("Сохранение таблицы в файл " + FILE_NAME);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(table);
                oos.flush();
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }


    public List<PlayerInfo> getTable() {
        return table;
    }

    public void add(String name, int score, int timeSeconds) {
        add(new PlayerInfo(name, score, timeSeconds));
    }

    public void add(PlayerInfo playerInfo) {
        boolean nameExists = false; // идея ругается, если не инициализировать
        for (int i =0; i<table.size(); i++) {
            if (table.get(i).name.equals(playerInfo.name)) { // Можно еще делать проверку были ли результаты лучше, но это уже вопрос дизайна
                table.set(i, playerInfo);
                nameExists = true;
                break;
            }
        }
        if (!nameExists) {
            table.add(playerInfo);
        }

        sort();
        save();
    }

    private void sort() {
        log.info("Сортировка таблицы рекордов");
        table = table.stream()
                .sorted(Comparator.comparing((PlayerInfo p) -> -p.score) // очки по убыванию
                .thenComparingInt(p->p.time)) // время по возрастанию
                .collect(Collectors.toList());
    }
}

