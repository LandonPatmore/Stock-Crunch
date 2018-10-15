package dataworkers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class DataGrabber {
    private static final Log logger = new Log(DataGrabber.class);

    /**
     * Grabs JSON data
     *
     * @param URL url to grab data from
     * @return JsonNode
     */
    public static JsonNode jsonGrabber(String URL) {
        try {
            final HttpResponse<JsonNode> response = Unirest.get(URL).asJson();
            logger.info("JSON data has been retrieved.", false);
            return response.getBody();
        } catch (UnirestException e){
            logger.error(e.getLocalizedMessage(), true);
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
            final Document document = Jsoup.parse(response.getBody(), "", Parser.xmlParser());
            logger.info("XML data has been retrieved.", false);
            return document;
        } catch (UnirestException e){
            logger.error(e.getLocalizedMessage(), true);
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
            final Document document = Jsoup.connect(url).get();
            logger.info("HTML data has been retrieved.", false);
            return document;
        } catch (IOException e){
            logger.error(e.getLocalizedMessage(), true);
            return null;
        }
    }
}
