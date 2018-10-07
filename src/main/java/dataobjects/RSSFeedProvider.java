package dataobjects;

public enum RSSFeedProvider {

    NASDAQ("NASDAQ"),
    MARKET_WATCH("MARKET WATCH"),
    NASDAQ_RSS_FEED("http://articlefeeds.nasdaq.com/"),
    MARKET_WATCH_FEED("https://www.marketwatch.com/rss/");

    private final String value;

    RSSFeedProvider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
