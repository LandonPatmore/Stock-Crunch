package dataobjects;

import org.joda.time.DateTime;
import org.jsoup.select.Elements;

public class Article {

    /**
     * Initial Data
     */
    private final String title;
    private final String link;
    private final DateTime dateTime;
    private final RSSFeedProvider providerName;

    /**
     * Data added later after parsing
     */

    private String author;
    private Elements body;
    private String copyright;
    // TODO: Sentiment analysis object added later

    // TODO: Get images from article as well
    public Article(String title, String link, DateTime dateTime, RSSFeedProvider providerName) {
        this.title = title;
        this.dateTime = dateTime;
        this.link = link;
        this.providerName = providerName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Elements getBody() {
        return body;
    }

    public void setBody(Elements body) {
        this.body = body;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getLink() {
        return link;
    }

    public RSSFeedProvider getProvider() {
        return providerName;
    }

    public RSSFeedProvider getProviderName() {
        return providerName;
    }
}
