package dataobjects;

public enum MarketWatch implements ArticleInterface {
    BREAKING_NEWS_BULELTINS("bulletins"),
    FINANCIAL("financial"),
    INTERNET_STORIES("internet"),
    MARKET_PULSE("marketpulse"),
    MUTUAL_FUNDS("mutualfunds"),
    PERSONAL_FINANCE("pf"),
    SOFTWARE_STORIES("software"),
    STOCKS_TO_WATCH("stockstowatch"),
    TOP_STORIES("topstories");

    private final String value;

    MarketWatch(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
