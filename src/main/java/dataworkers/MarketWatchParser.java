package dataworkers;

import dataobjects.Article;
import dataobjects.MarketWatch;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class MarketWatchParser {
    private static final String BASE_URL = "https://www.marketwatch.com/rss/";

    public static void grabArticles(MarketWatch marketWatch) {
        final String url = BASE_URL + marketWatch.getEndpoint();
        final Document xml = DataFetcher.xmlGrabber(url);

        if (xml != null) {
            final Elements articles = xml.select("channel").get(0).select("item");

            for(Element e : articles){
                System.out.println(e.select("title").first());
            }
        }
    }
}
