package dataworkers;

import dataobjects.Article;
import dataobjects.ArticleTypes;
import dataobjects.MarketWatch;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ArticleListFetcher {

    private static final DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("EEE, dd MMM YYYY HH:mm:ss z").getParser(),
            DateTimeFormat.forPattern("EEE, dd MMM YYYY HH:mm:ss Z").getParser()};
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    public static ArrayList<String> grabArticles(String url, ArticleTypes typeOfFeed) {
        final Document xml = DataFetcher.xmlGrabber(url + typeOfFeed.getEndpoint());

        try {
            if (xml != null) {
                final Elements articles = xml.select("channel").get(0).select("item");

                for (Element e : articles) {
                    final String title = e.select("title").first().text();
                    final URL link = new URL(e.select("link").first().text());
                    final DateTime pubDate = formatter.parseDateTime(e.select("pubDate").first().text());
                    System.out.println(e.select("description").first().text());

//                    System.out.println(new Article(title, link, pubDate));
                }
            }

            return null;
        } catch (MalformedURLException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
