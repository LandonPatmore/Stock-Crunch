package dataobjects;

import javafx.scene.image.Image;
import org.joda.time.DateTime;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;

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
    private ArticleSentiment sentiment;
    private Image image;

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public void setSentiment(ArticleSentiment sentiment) { this.sentiment = sentiment;}

    public String getSentiment() {
        final DecimalFormat df = new DecimalFormat("0.##");
        final double rand = Math.random();
        if(rand > 0.5)
            return df.format(rand * 100) + "% " + "bullish";
        else
            return df.format(rand * 100) + "% " + "bearish";
    }

    public String getDateForChange(){
        if(dateTime.getDayOfMonth() < 10)
            return dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-0" + dateTime.getDayOfMonth();
        else
            return dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth();
    }

}
