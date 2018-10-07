package dataobjects;

public enum FeedProvider{

    YAHOO("YAHOO"),
    MARKET_WATCH("MARKET WATCH"),
    YAHOO_URL("http://finance.yahoo.com/rss/"),
    MARKET_WATCH_URL("https://www.marketwatch.com/rss/");

    private final String value;

    FeedProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
