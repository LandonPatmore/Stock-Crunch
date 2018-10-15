package dataobjects;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Log;

import java.io.*;
import java.util.ArrayList;

public class Favorites {
    private static final Log logger = new Log(Favorites.class);

    private static final File file = new File("favorites.ser");

    private static boolean checkFileExists() {
        logger.debug("Does favorites file exist? : " + file.exists(), false);
        return file.exists();
    }

    private static boolean createFavoritesFile() {
        try {
            logger.debug("New favorites file will be created.", false);
            return file.createNewFile();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), true);
            return false;
        }
    }

    public static void serializeData(ObservableList<String> favorites) {
        new Thread(() -> {
            if (!checkFileExists()) {
                createFavoritesFile();
            }

            try {
                final FileOutputStream f = new FileOutputStream(file);
                final ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(new ArrayList<>(favorites));

                o.close();
                f.close();

                logger.debug("Favorites file data has been serialized.", false);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), true);
            }
        }).start();
    }

    public static ObservableList<String> readData() {
        if (!checkFileExists()) {
            createFavoritesFile();
        }
        try {
            final FileInputStream f = new FileInputStream(file);
            final ObjectInputStream o = new ObjectInputStream(f);

            final ArrayList<String> list = new ArrayList<>((ArrayList<String>) o.readObject());

            f.close();
            o.close();

            logger.debug("Favorites file data has been read.", false);
            return FXCollections.observableArrayList(list);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), true);
            return null;
        }
    }
}
