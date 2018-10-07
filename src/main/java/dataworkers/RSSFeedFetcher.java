package dataworkers;

import dataobjects.Article;
import dataobjects.RSSFeedProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class RSSFeedFetcher {

    private static final DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("EEE, dd MMM YYYY HH:mm:ss z").getParser(),
            DateTimeFormat.forPattern("EEE, dd MMM YYYY HH:mm:ss Z").getParser()};
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    public static ArrayList<Article> grabArticles(RSSFeedProvider provider, RSSFeedProvider providerURL, String appendedData) {
        final ArrayList<Article> articleList = new ArrayList<>();
        final Document xml = DataFetcher.xmlGrabber(providerURL.getValue() + appendedData);

        if (xml != null) {

            final Elements articles = xml.select("channel").first().select("item");



            for (Element e : articles) {
                final String title = e.select("title").first().text();
                final String link = e.select("link").first().text();
                final DateTime pubDate = formatter.parseDateTime(e.select("pubDate").first().text());

                articleList.add(new Article(title, link, pubDate, provider));
            }
        }

        return articleList;
    }
}
