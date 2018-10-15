package dataworkers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class DataGrabber {

    /**
     * Grabs JSON data
     *
     * @param URL url to grab data from
     * @return JsonNode
     */
    public static JsonNode jsonGrabber(String URL) {
        try {
            final HttpResponse<JsonNode> response = Unirest.get(URL).asJson();
            return response.getBody();
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Grabs XML data
     *
     * @param URL url to grab data from
     * @return Document
     */
    public static Document xmlGrabber(String URL) {
        try {
            final HttpResponse<String> response = Unirest.get(URL).asString();

            return Jsoup.parse(response.getBody(), "", Parser.xmlParser());
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Grabs HTML data
     *
     * @param url url to grab data from
     * @return Document
     */
    public static Document htmlGrabber(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
}
