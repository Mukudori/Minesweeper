package ru.minesweeper.view.images;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class ImageLoader {
    private final String filePath;
    public ImageLoader(String filePath){
        this.filePath = filePath;
    }

    public Optional<ImageIcon> get() throws IOException {
        Optional<URL> url = Optional.of(getClass().getResource(filePath));

        if (url.isPresent()){
            Image img = ImageIO.read(url.get());
            return Optional.of(new ImageIcon(img));
        }

        return Optional.empty();


    }
}
