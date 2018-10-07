package dataworkers;

import dataobjects.Article;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MarketWatchArticleParser {

    public static boolean getArticleData(Article article) {
        final Document articleData = DataFetcher.htmlGrabber(article.getLink());

        if(articleData != null) {
            final String author = articleData.getElementById("author-bylines").select("a").text();
            final Elements body = articleData.getElementById("article-body").select("p");
            final String copyright = articleData.getElementsByClass("copyright").text();

            article.setAuthor(author);
            article.setBody(body);
            article.setCopyright(copyright);

            return true;
        }

        return false;
    }

}

