package dataworkers;

import dataobjects.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class MarketWatchArticleParser {

    public static Boolean getArticleData(Article a) {

        Document doc = DataFetcher.htmlGrabber(a.getLink());

        if (doc != null) {
            a.setBody(doc.getElementById("article-body").getAllElements().text());
            a.setAuthor(doc.getElementById("author-bylines").attr("rel",
                    "author").text());
            a.setCopyright(doc.select("p.copyright").text());

            return true;
        }

        return false;
    }

}

