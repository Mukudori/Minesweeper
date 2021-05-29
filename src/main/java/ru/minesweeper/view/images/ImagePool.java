package ru.minesweeper.view.images;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class ImagePool {
    private static Map<ImageType, ImageIcon> iconMap = new EnumMap<>(ImageType.class);
    private static final Logger log = LoggerFactory.getLogger(ImagePool.class);
    private ImagePool() {}



    public static ImageIcon get(ImageType type) {
        if (!iconMap.containsKey(type)) {
            ImageLoader loader = new ImageLoader(type.getFilePath());
            try {
                Optional<ImageIcon> imageIcon = loader.get();
                imageIcon.ifPresent(icon -> iconMap.put(type, icon));

            } catch (IOException e) {
                log.warn(e.getMessage());
            }
        }
        return iconMap.get(type);
    }

    public static Optional<ImageIcon> get(String val){
        Optional<ImageType> type;
        try {
             type = Optional.of(ImageType.valueOf("NUM_"+val));
             return Optional.of(get(type.get()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        try {
            type = Optional.of(ImageType.valueOf(val));
            return Optional.of(get(type.get()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return Optional.empty();
    }



}
