package dataworkers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.java2d.jules.JulesPathBuf;

import java.text.DecimalFormat;

public class StockFetcher {

    /*static ArrayList<JSONObject> stockDataHistorical(String url){
        ArrayList<JSONObject> stocks = new ArrayList<>();
        try {
            String test = Jsoup.connect("https://www.nasdaq.com/symbol/" + url + "/historical").get().toString();
            Document stock = Jsoup.parse(test, "UTF-8", Parser.htmlParser());
            Elements dataTable = stock.getElementsByClass("genTable");
            JSONObject data;
            Element realShit = dataTable.get(0).child(0).child(1).child(1);

            for(Element tr: realShit.children()){
                data = new JSONObject();
                if(!tr.child(0).text().equals("")){
                    data.put("date", tr.child(0).text());
                    data.put("open", tr.child(1).text());
                    data.put("high", tr.child(2).text());
                    data.put("low", tr.child(3).text());
                    data.put("close", tr.child(4).text());
                    data.put("volume", tr.child(5).text());
                    stocks.add(data);
                }
            }

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        return stocks;
    }*/

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
            Document test = Jsoup.connect(realURL).ignoreContentType(true).get();

            data = new JSONArray(test.text());
        } catch (Exception e) {
            //System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
        return data;
    }


    public static JSONObject stockDataCurrent(String ticker) {
        JSONObject data;
        try {
            Document rawData = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + ticker +
                    "/quote").ignoreContentType(true).get();

            data = new JSONObject(rawData.text());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        return data;
    }

    public static String changeSince(String thenDate, String ticker) {

        JSONArray ary = StockFetcher.stockDataHistorical(ticker, 1, "m");

        double change = 0.0;

        try {
            double[] prices = getPricesFromDate(ary, thenDate, ticker);
            double thenPrice = prices[0];
            double nowPrice = prices[1];

            if (thenPrice < Double.MAX_VALUE && nowPrice < Double.MAX_VALUE) {
                DecimalFormat df = new DecimalFormat("0.##");
                return df.format(calculateChange(thenPrice, nowPrice)) + " %";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static double[] getPricesFromDate(JSONArray obj, String date, String ticker) {

        double[] prices = new double[2];
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

        if (!found)
            prices[0] = prices[1] = Double.MAX_VALUE;

        return prices;
    }

    private static double calculateChange(double then, double now) {

        if (now == 0.0)
            return (-then * 100);

        return (then - now) / now * 100;

    }


    public static void main(String[] args) {
        System.out.println(changeSince("2018-09-07", "tsla"));

        JSONObject test = StockFetcher.stockDataCurrent("aapl");
        System.out.println("test");
    }

}
