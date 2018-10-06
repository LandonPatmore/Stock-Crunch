package engine;

import dataobjects.MarketWatch;
import dataworkers.DataFetcher;
import dataworkers.MarketWatchParser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Data Mea");
        Pane root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        primaryStage.setScene(new Scene(root));
        root.requestFocus();
        primaryStage.show();
        //DashboardLogin.setStage(primaryStage);
    }
}