package dataobjects;

public enum YahooArticle implements ArticleInterface {
    HEADLINE("headline?s=");


    private final String value;

    YahooArticle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
