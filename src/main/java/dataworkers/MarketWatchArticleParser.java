package dataworkers;

import dataobjects.Article;
import org.jsoup.nodes.Document;

public class MarketWatchArticleParser implements ArticleParserInterface{

    @Override
    public Article getArticleData(String url) {
        final Document article = DataFetcher.htmlGrabber(url);


        return null;
    }
}
