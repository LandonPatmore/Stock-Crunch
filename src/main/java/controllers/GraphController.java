package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.json.JSONArray;
import org.json.JSONObject;

public class GraphController {

    public static XYChart.Series getGraphData(JSONArray array, int totalNum){
        XYChart.Series series = new XYChart.Series();
        int spacer = array.length() / totalNum;
        for(int i = 0; i < totalNum; i+=spacer){
            if(i  == totalNum -1){
                series.getData().add(new XYChart.Data(((JSONObject)array.get(i)).get("date"), ((JSONObject)array.get(i)).get("vwap")));
            }
            else{
                series.getData().add(new XYChart.Data(((JSONObject)array.get(i)).get("date"), ((JSONObject)array.get(i)).get("vwap")));
            }
        }
        return series;
    }
}