package dataworkers;

import dataobjects.Article;

public interface ArticleParserInterface {

    Article getArticleData(String url);
}
