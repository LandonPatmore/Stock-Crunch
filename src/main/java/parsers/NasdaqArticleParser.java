package parsers;

import dataobjects.Article;
import dataworkers.DataFetcher;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NasdaqArticleParser {

    public static boolean getArticleData(Article article) {
        final Document articleData = DataFetcher.htmlGrabber(article.getLink());

        if(articleData != null) {
            final String author = articleData.getElementsByClass("article-byline").select("span").get(1).text();
            final Elements body = articleData.getElementById("articlebody").select("p");

            article.setAuthor(author);
            article.setBody(body);

            return true;
        }

        return false;
    }
}