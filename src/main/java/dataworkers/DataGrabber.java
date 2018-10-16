package dataworkers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import dataobjects.CSVObject;
import model.Log;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

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
     * @param URL url to grab data from
     * @return Document
     */
    public static Document htmlGrabber(String URL) {
        try {
            final Document document = Jsoup.connect(URL).get();
            logger.info("HTML data has been retrieved.", false);
            return document;
        } catch (IOException e){
            logger.error(e.getLocalizedMessage(), true);
            return null;
        }
    }

    /**
     * Grabs CSV data
     *
     * @param URL url to grab data from
     * @param columnNames list of column names to parse out
     * @return ArrayList<CSVObject>
     */
    public static ArrayList<CSVObject> csvGrabber(String URL, String ... columnNames) {
        final ArrayList<CSVObject> csvObjects = new ArrayList<>();

        for(String column : columnNames){
            csvObjects.add(new CSVObject(column, new ArrayList<>()));
        }

        try{
            final InputStream response = Unirest.get(URL).asBinary().getBody();

            final Reader reader = new InputStreamReader(response);
            final CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            try {
                for (CSVRecord record : parser) {
                    for(String column : columnNames){
                        for(CSVObject csv : csvObjects){
                            if(column.equals(csv.getColumnName())){
                                csv.getData().add(record.get(column));
                                break;
                            }
                        }
                    }
                }
            } finally {
                parser.close();
                reader.close();
            }

            return csvObjects;
        } catch (UnirestException | IOException e) {
            logger.error(e.getLocalizedMessage(), true);
            return null;
        }
    }
}
