package dataobjects;

import org.joda.time.DateTime;

public class Article {

    private final String title;
    private final String author;
    private final String provider;
    private final DateTime dateTime;

    // TODO: Get images from article as well
    public Article(String title, String author, String provider, DateTime dateTime){
        this.title = title;
        this.author = author;
        this.dateTime = dateTime;
        this.provider = provider;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getProvider() {
        return provider;
    }
}
