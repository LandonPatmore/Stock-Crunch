package dataworkers;

import org.json.XML;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

import java.util.ArrayList;

public class SeekingAlphaParser {
    //have to add (name).xml to end
    static String url = "https://seekingalpha.com/api/sa/combined/";

    public static Document getXML(String url){
        return DataFetcher.xmlGrabber("https://seekingalpha.com/api/sa/combined/" + url + ".xml");
    }

    public static ArrayList<String> articleFinder(String url) throws ParserConfigurationException, IOException, SAXException {
        try {
            ArrayList<String> newsLinks = new ArrayList<>();

            Document xml = DataFetcher.xmlGrabber("https://seekingalpha.com/api/sa/combined/" + url + ".xml");
            System.out.println(xml);

           /* Document document = Jsoup.parse(xml, "", Parser.xmlParser());*/

            Elements links = xml.select("link");

            for(Element e: links){
                if(!newsLinks.contains(e.text())){
                    if(e.text().contains("article")){
                        newsLinks.add(e.text());
                        System.out.println(e.text());
                    }
                }
            }

            return newsLinks;
        } catch (Exception e){

        }
        return null;
    }

    public static void main(String[]args){
        try {
            SeekingAlphaParser.articleFinder("IBM");
        }catch (Exception e){

        }
    }
}
