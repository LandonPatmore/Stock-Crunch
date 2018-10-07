package dataobjects;

public enum NasdaqArticleRSSFeed implements ArticleInterface {
    SYMBOL("nasdaq/symbols?symbol=");

    private final String value;

    NasdaqArticleRSSFeed(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
