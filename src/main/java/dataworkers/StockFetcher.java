package dataworkers;

import model.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.java2d.jules.JulesPathBuf;

import java.text.DecimalFormat;
import java.util.Arrays;

public class StockFetcher {
    private static final Log logger = new Log(StockFetcher.class);

    public static JSONArray stockDataHistorical(String url, int number, String timeframe) {
        JSONArray data;
        String realURL = "https://api.iextrading.com/1.0/stock/" + url;
        switch (timeframe) {
            case "d":
                realURL += "/chart/" + number + "d";
                break;
            case "m":
                realURL += "/chart/" + number + "m";
                break;
            case "y":
                realURL += "/chart/" + number + "y";
                break;
        }
        try {
            //Document test = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + url + "/chart/1y").get();
            final Document t = Jsoup.connect(realURL).ignoreContentType(true).get();

            data = new JSONArray(t.text());

            logger.debug("Stock historical data: " + data, false);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),true);
            return null;
        }
        return data;
    }


    public static JSONObject stockDataCurrent(String ticker) {
        JSONObject data;
        try {
            final Document rawData = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + ticker +
                    "/quote").ignoreContentType(true).get();

            data = new JSONObject(rawData.text());

            logger.debug("Stock current data: " + data, false);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),true);
            return null;
        }
        return data;
    }

    public static String changeSince(String thenDate, String ticker) {
        final JSONArray ary = StockFetcher.stockDataHistorical(ticker, 1, "m");

        try {
            final double[] prices = getPricesFromDate(ary, thenDate, ticker);
            final double thenPrice = prices[0];
            final double nowPrice = prices[1];

            if (thenPrice < Double.MAX_VALUE && nowPrice < Double.MAX_VALUE) {
                final DecimalFormat df = new DecimalFormat("0.##");
                final String changeSince = df.format(calculateChange(thenPrice, nowPrice)) + " %";
                logger.debug("Change since: " + changeSince, false);
                return changeSince;
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),true);
        }
        return null;
    }

    public static double[] getPricesFromDate(JSONArray obj, String date, String ticker) {

        final double[] prices = new double[2];
        String d;
        boolean found = false;
        JSONObject cur;

        for (int i = 0; i < obj.length(); i++) {
            cur = obj.getJSONObject(i);
            d = cur.getString("date");

            if (found) {
                prices[1] = cur.getDouble("vwap");
                break;
            }

            if (!d.equals("")) {
                prices[0] = cur.getDouble("vwap");
                found = true;
            }
        }

        if (!found) {
            prices[0] = prices[1] = Double.MAX_VALUE;
        }

        logger.debug("Prices: " + Arrays.toString(prices), false);
        return prices;
    }

    private static double calculateChange(double then, double now) {
        if (now == 0.0) {
            return (-then * 100);
        }

        final double change = (then - now) / now * 100;
        logger.debug("Change: " + change, false);
        return change;
    }

}
