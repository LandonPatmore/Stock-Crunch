package dataobjects;

import java.text.DecimalFormat;

public class ArticleSentiment {
    private int[] scores;
    private static int overallSentiment;
    private static double sentimentPct;
    private static int sentencesAnalyzed;

    public ArticleSentiment(int[] scores){
        this. scores = scores;
        overallSentiment = scores[4] * 2 + scores[3] - scores[1] - scores[0] * 2;
        sentencesAnalyzed = scores[4] + scores[3] + scores[2] + scores[1] + scores[0];

        if(sentencesAnalyzed > 0) sentimentPct = ((double)overallSentiment / sentencesAnalyzed) * 100;
        else sentimentPct = 0;
    }

    public String getSentiment(){
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(sentimentPct) + "%";
    }
}
