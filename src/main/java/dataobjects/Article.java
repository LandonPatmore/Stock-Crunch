package dataobjects;

import org.joda.time.DateTime;

import java.net.URL;

public class Article {

    private final String title;
    private final URL provider;
    private final DateTime dateTime;
    private final FeedProvider providerName;

    // TODO: Get images from article as well
    public Article(String title, URL provider, DateTime dateTime, FeedProvider providerName){
        this.title = title;
        this.dateTime = dateTime;
        this.provider = provider;
        this.providerName = providerName;
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
