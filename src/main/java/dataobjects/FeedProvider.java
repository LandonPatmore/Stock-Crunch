package dataobjects;

public enum FeedProvider implements ArticleInterface{

    YAHOO("http://finance.yahoo.com/rss/"),
    MARKET_WATCH("https://www.marketwatch.com/rss/");

    private final String value;

    FeedProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
