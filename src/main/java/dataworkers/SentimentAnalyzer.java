package dataworkers;

import dataobjects.Article;
import dataobjects.ArticleSentiment;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

public class SentimentAnalyzer {

    private static int[] articleScore = new int[5];

    public static void getSentimentScore(Article a) {

        String[] sentences = getSentences(a);

        int sentenceScore;

        for(String s : sentences){
            sentenceScore = analyzeSentiment(s);
            if(sentenceScore > 0)
                articleScore[sentenceScore]++;
        }

        ArticleSentiment sentiment = new ArticleSentiment(articleScore);

        a.setSentiment(sentiment);
    }

    private static int analyzeSentiment(String sentence) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(sentence);
        List<CoreMap> cm = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        int sentimentScore = -1;

        for (CoreMap s : cm) {
            Tree tree = s.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            sentimentScore = RNNCoreAnnotations.getPredictedClass(new CoreLabel(tree.label()));
        }

        return sentimentScore;
    }

    private static String[] getSentences(Article a) {

        String text = "";

        if(a.getBody() != null)
            text = a.getBody().text();

        text = text.replace(". ", ".~");
        text = text.replace("? ", ".~");
        text = text.replace("! ", ".~");

        String[] sentences = text.split("~");

        return sentences;
    }
}
