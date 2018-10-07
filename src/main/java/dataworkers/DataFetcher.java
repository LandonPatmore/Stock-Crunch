package dataworkers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;

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

    public static Document xmlGrabber(String URL) {
        try {
            final HttpResponse<String> response = Unirest.get(URL).asString();

            return Jsoup.parse(response.getBody(), "", Parser.xmlParser());
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static Document htmlGrabber(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
