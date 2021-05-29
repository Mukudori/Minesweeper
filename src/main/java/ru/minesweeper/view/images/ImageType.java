package ru.minesweeper.view.images;

public enum ImageType {
    NUM_0("0"),
    NUM_1("1"),
    NUM_2("2"),
    NUM_3("3"),
    NUM_4("4"),
    NUM_5("5"),
    NUM_6("6"),
    NUM_7("7"),
    NUM_8("8"),
    BOMB("bomb"),
    EMOJI_CLICK("emoji_click"),
    EMOJI_DEAD("emoji_dead"),
    EMOJI_IDLE("emoji_idle"),
    FLAG("flag");

    private static final String PATH = "/pics/";
    private static final String FORMAT = ".png";
    private final String fileName;


    ImageType(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return PATH +fileName+ FORMAT;
    }

    public String getFileName() {
        return fileName;
    }
}
