package engine;

import dataobjects.Article;
import dataobjects.MarketWatchRSSFeed;
import dataobjects.RSSFeedProvider;
import dataworkers.DataFetcher;
import dataworkers.RSSFeedFetcher;
import dataworkers.SentimentAnalyzer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import parsers.MarketWatchArticleParser;
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
        primaryStage.setTitle("Stock Crunch");
        Pane root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        primaryStage.setScene(new Scene(root));
        root.requestFocus();
        primaryStage.show();

        //demo of sentiment analysis
//        ArrayList<Article> articles = RSSFeedFetcher.grabArticles(RSSFeedProvider.MARKET_WATCH,
//                RSSFeedProvider.MARKET_WATCH_FEED, MarketWatchRSSFeed.BREAKING_NEWS_BULELTINS.getValue());
//        for(Article a : articles){
//            MarketWatchArticleParser.getArticleData(a);
//            SentimentAnalyzer.getSentimentScore(a);
//            if(a.getBody() != null) {
//                System.out.println(a.getBody().text());
//                System.out.println(a.getSentiment());
//            }
//        }

        //DashboardLogin.setStage(primaryStage);
    }
}