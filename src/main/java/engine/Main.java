package engine;

import dataobjects.Article;
import dataobjects.FeedProvider;
import dataobjects.MarketWatch;
import dataworkers.ArticleListFetcher;
import dataworkers.MarketWatchArticleParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<Article> articles = new ArrayList<>();
        articles = ArticleListFetcher.grabArticles(FeedProvider.MARKET_WATCH, FeedProvider.MARKET_WATCH_URL,
                MarketWatch.SOFTWARE_STORIES);

        //DashboardLogin.setStage(primaryStage);
    }
}