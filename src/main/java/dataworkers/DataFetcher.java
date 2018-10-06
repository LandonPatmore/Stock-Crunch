package dataworkers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class DataFetcher {

    // TODO: Add authentication as well for endpoints

    public static JsonNode jsonGrabber(String URL) {
        try {
            final HttpResponse<JsonNode> response = Unirest.get(URL).asJson();
            return response.getBody();
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static String xmlGrabber(String URL) {
        try {
            final HttpResponse<String> response = Unirest.get(URL).asString();

            return response.getBody();
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
