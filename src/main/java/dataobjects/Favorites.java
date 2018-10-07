package dataobjects;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class Favorites {

    private static final File file = new File("favorites.ser");

    private static boolean checkFileExists() {
        try {
            return file.exists();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    private static boolean createFavoritesFile() {
        try {
            return file.createNewFile();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    public static void serializeData(ObservableList<String> favorites) {
        if (!checkFileExists()) {
            createFavoritesFile();
        }
        Platform.runLater(() -> {
            try {
                final FileOutputStream f = new FileOutputStream(file);
                final ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(new ArrayList<>(favorites));

                o.close();

                o.close();
                f.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    public static ObservableList<String> readData() {
        if (!checkFileExists()) {
            createFavoritesFile();
        }
        try {
            final FileInputStream f = new FileInputStream(file);
            final ObjectInputStream o = new ObjectInputStream(f);
            ArrayList<String> list = new ArrayList<>();

            list.addAll((ArrayList<String>) o.readObject());

            f.close();
            o.close();

            return FXCollections.observableArrayList(list);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
