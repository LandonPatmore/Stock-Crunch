package dataobjects;

public enum YahooArticle implements ArticleTypes {

    HEADLINE("headline?s=");


    private final String endpoint;

    YahooArticle(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }
}
