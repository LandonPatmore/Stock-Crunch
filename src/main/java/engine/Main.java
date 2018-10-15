package engine;

import controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Log;

import java.io.IOException;

public class Main extends Application {
    private static final Log logger = new Log(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        HomeController.setEs(getHostServices());
        primaryStage.setTitle("Stock Crunch");
        Pane root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        primaryStage.setScene(new Scene(root));
        root.requestFocus();
        logger.info("Preparing to show primary stage", false);
        primaryStage.show();
        logger.info("Primary stage is showing", false);
    }
}