package controllers;

import javafx.scene.chart.XYChart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphController {

    public static XYChart.Series getGraphData(JSONArray array, int totalNum){
        XYChart.Series series = new XYChart.Series();
        int spacer = array.length() / totalNum;
        String average;
        try{
            ((JSONObject)array.get(0)).get("vwap");
            average = "vwap";
            for(int i = 0; i < array.length(); i+=spacer){
                if(i  == totalNum -1){
                    series.getData().add(new XYChart.Data(((JSONObject)array.get(array.length()-1)).get("date").toString(), Double.valueOf(((JSONObject)array.get(i)).get(average).toString())));
                }
                else{
                    series.getData().add(new XYChart.Data(((JSONObject)array.get(i)).get("date"), Double.valueOf(((JSONObject)array.get(i)).get(average).toString())));
                }
            }

        }catch (JSONException e){
            average = "average";
            for(int i = 0; i < array.length(); i+=spacer){
                if(i  == totalNum -1){
                    series.getData().add(new XYChart.Data(((JSONObject)array.get(array.length()-1)).get("minute").toString(), Double.valueOf(((JSONObject)array.get(i)).get(average).toString())));
                }
                else{
                    series.getData().add(new XYChart.Data(((JSONObject)array.get(i)).get("minute").toString(), Double.valueOf(((JSONObject)array.get(i)).get(average).toString())));
                }
            }
        }

        return series;
    }
}