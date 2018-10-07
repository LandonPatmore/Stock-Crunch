package dataobjects;

import org.joda.time.DateTime;

import java.net.URL;

public class Article {

    /**
     * Initial Data
     */
    private final String title;
    private final URL provider;
    private final DateTime dateTime;
    private final FeedProvider providerName;

    /**
     * Data added later after parsing
     */

    private String author;
    private String body;
    private String copyright;
    // TODO: Sentiment analysis object added later

    // TODO: Get images from article as well
    public Article(String title, URL provider, DateTime dateTime, FeedProvider providerName) {
        this.title = title;
        this.dateTime = dateTime;
        this.provider = provider;
        this.providerName = providerName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
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

    public URL getLink() {
        return provider;
    }

    public URL getProvider() {
        return provider;
    }

    public FeedProvider getProviderName() {
        return providerName;
    }
}
