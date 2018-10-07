package engine;

import dataobjects.Article;
import dataobjects.RSSFeedProvider;
import dataobjects.NasdaqArticleRSSFeed;
import dataworkers.RSSFeedFetcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import parsers.NasdaqArticleParser;

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
        ArrayList<Article> articles = RSSFeedFetcher.grabArticles(RSSFeedProvider.NASDAQ, RSSFeedProvider.NASDAQ_RSS_FEED,
                NasdaqArticleRSSFeed.SYMBOL.getValue() + "mcd");

        for(Article article : articles){
            if(NasdaqArticleParser.getArticleData(article)) {
                System.out.println(article.getBody());

                System.out.println("\n\n");
            }
        }

        //DashboardLogin.setStage(primaryStage);
    }
}