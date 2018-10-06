package dataworkers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class YahooNewsParser {

    static String URL = "http://finance.yahoo.com/rss/headline?s=";

    public static String getXml(ArrayList<String> tickers){
        String ticker = "";
        for(String t : tickers)
            ticker += t + ",";
        ticker = ticker.substring(0, ticker.length() - 1);
        URL += ticker;
        return DataFetcher.xmlGrabber(URL);
    }

    public static String getXml(String ticker){
        URL += ticker;
        return DataFetcher.xmlGrabber(URL);
    }

    public static Document getRss(String ticker){
        URL += ticker;
        return Jsoup.parse(DataFetcher.xmlGrabber(URL));
    }

    public static String getTitle(String ticker){
        return getRss(ticker).title();
    }

    public static String getAttr(String ticker, String attr){
        return getRss(ticker).attr(attr);
    }

    public static String getBody(String ticker){
        return getRss(ticker).body().toString();
    }

    public static String getHead(String ticker){
        return getRss(ticker).head().toString();
    }

}

