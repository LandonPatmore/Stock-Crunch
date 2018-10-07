package parsers;

import dataobjects.Article;
import dataworkers.DataFetcher;
import javafx.scene.image.Image;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MarketWatchArticleParser {

    public static boolean getArticleData(Article article) {
        final Document articleData = DataFetcher.htmlGrabber(article.getLink());

        if(articleData != null) {
            try {
                final String author = articleData.getElementById("author-bylines").select("a").text();
                final Elements body = articleData.getElementById("article-body").select("p");
                final String copyright = articleData.getElementsByClass("copyright").text();
                final Image image = new Image(articleData.getElementsByClass("article-image").first().attr("src"));

                article.setAuthor(author);
                article.setBody(body);
                article.setCopyright(copyright);
                article.setImage(image);
            }
            catch (NullPointerException e){
                return false;
            }

            return true;
        }

        return false;
    }

}

