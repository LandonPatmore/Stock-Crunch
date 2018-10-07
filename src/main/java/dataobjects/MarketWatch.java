package dataobjects;

public enum MarketWatch implements ArticleTypes {
    BREAKING_NEWS_BULELTINS("bulletins"),
    FINANCIAL("financial"),
    INTERNET_STORIES("internet"),
    MARKET_PULSE("marketpulse"),
    MUTUAL_FUNDS("mutualfunds"),
    PERSONAL_FINANCE("pf"),
    REAL_TIME_HEADLINES("realtimeheadlines"),
    SOFTWARE_STORIES("software"),
    STOCKS_TO_WATCH("stockstowatch"),
    TOP_STORIES("topstories");

    private final String endpoint;

    MarketWatch(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }


}
