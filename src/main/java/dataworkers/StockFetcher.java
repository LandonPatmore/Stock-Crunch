package dataworkers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;

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

    static JSONArray stockDataHistorical(String url, int number, String timeframe){
        JSONArray data;
        String realURL = "https://api.iextrading.com/1.0/stock/" + url;
        switch(timeframe){
            case "m":
                realURL+="/chart/"+number+"m";
                break;
            case "y":
                realURL+="/chart/"+number+"y";
                break;
        }
        try {
            //Document test = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + url + "/chart/1y").get();
            Document test = Jsoup.connect(realURL).ignoreContentType(true).get();

            data = new JSONArray(test.text());
        }catch (Exception e){
            //System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
        return data;
    }


    static JSONObject stockDataCurrent(String url){
        JSONObject data;
        try{
            Document rawData = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + url + "/quote").ignoreContentType(true).get();

            data = new JSONObject(rawData.text());

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }



        return data;
    }


    public static void main(String[]args){
        JSONArray name = StockFetcher.stockDataHistorical("aapl", 5, "y");
        System.out.println("test");

        JSONObject test = StockFetcher.stockDataCurrent("aapl");
        System.out.println("test");
    }
}
