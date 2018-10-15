package parsers;

import dataobjects.Article;
import dataworkers.DataGrabber;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ArticleParser {

    /**
     * Parses data from Market Watch article
     *
     * @param article article to be scraped
     * @return if the article data was actually scraped
     */
    public static boolean marketWatchParser(Article article) {
        final Document articleData = DataGrabber.htmlGrabber(article.getLink());

        if(articleData != null) {
            try {
                final String author = articleData.getElementById("author-bylines").select("a").text();
                final Elements body = articleData.getElementById("article-body").select("p");
                final String copyright = articleData.getElementsByClass("copyright").text();
//                final Image image = new Image(articleData.getElementsByClass("article-image").first().attr("src"));

                article.setAuthor(author);
                article.setBody(body);
                article.setCopyright(copyright);
//                article.setImage(image);
            }
            catch (NullPointerException e){
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Parses data from Nasdaq article
     *
     * @param article article to be scraped
     * @return if the article data was actually scraped
     */
    public static boolean nasdaqParser(Article article) {
        final Document articleData = DataGrabber.htmlGrabber(article.getLink());

        if(articleData != null) {
            final String author = articleData.getElementsByClass("article-byline").select("span").get(1).text();
            final Elements body = articleData.getElementById("articlebody").select("p");
//            final Image image = new Image(articleData.getElementsByClass("article-image").first().select("img").attr("src"));

            article.setAuthor(author);
            article.setBody(body);
//            article.setImage(image);

            return true;
        }

        return false;
    }
}
