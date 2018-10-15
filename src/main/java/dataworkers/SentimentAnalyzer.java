package dataworkers;

import dataobjects.Article;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import model.Log;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

public class SentimentAnalyzer {
    private static final Log logger = new Log(SentimentAnalyzer.class);

    private static int[] articleScore = new int[5];

    public static void getSentimentScore(Article a) {
        logger.info("Starting sentiment analysis on article: " + a.getTitle(), false);

        String[] sentences = getSentences(a);

        int sentenceScore;

        for (String s : sentences) {
            sentenceScore = analyzeSentiment(s);
            if (sentenceScore > 0)
                articleScore[sentenceScore]++;
        }

        a.setSentiment(getSentiment(articleScore));

        logger.info("Sentiment: " + a.getSentiment(), false);

        logger.info("Finished sentiment analysis on article: " + a.getTitle(), false);
    }

    private static int analyzeSentiment(String sentence) {

        final Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        final Annotation annotation = pipeline.process(sentence);
        final List<CoreMap> cm = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        int sentimentScore = -1;

        for (CoreMap s : cm) {
            Tree tree = s.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            sentimentScore = RNNCoreAnnotations.getPredictedClass(new CoreLabel(tree.label()));
        }

        return sentimentScore;
    }

    private static String[] getSentences(Article a) {

        String text = "";

        if (a.getBody() != null)
            text = a.getBody().text();

        text = text.replace(". ", ".~");
        text = text.replace("? ", ".~");
        text = text.replace("! ", ".~");

        return text.split("~");
    }

    private static String getSentiment(int[] scores) {
        final DecimalFormat df = new DecimalFormat("0.##");
        final int overallSentiment = scores[4] * 2 + scores[3] - scores[1] - scores[0] * 2;
        final int sentencesAnalyzed = scores[4] + scores[3] + scores[2] + scores[1] + scores[0];
        double sentimentPct;
        boolean bullish;

        if (sentencesAnalyzed > 0) {
            sentimentPct = ((double) overallSentiment / sentencesAnalyzed) * 100;
        } else {
            sentimentPct = 0;
        }

        bullish = sentimentPct > 0;
        sentimentPct = Math.abs(sentimentPct);

        if (bullish) {
            return df.format(sentimentPct) + "% " + "bullish";
        } else {
            return df.format(sentimentPct) + "% " + "bearish";
        }
    }
}
