package dataworkers;

import dataobjects.Article;
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
    static String url = "http://seekingalpha.com/api/sa/combined/";

    public static Document getXML(String url){
        return DataFetcher.xmlGrabber("http://seekingalpha.com/api/sa/combined/" + url + ".xml");
    }

    public static ArrayList<String> articleFinder(String url) throws ParserConfigurationException, IOException, SAXException {
        try {
            ArrayList<String> newsLinks = new ArrayList<>();

            Document xml = DataFetcher.xmlGrabber("http://seekingalpha.com/api/sa/combined/" + url + ".xml");
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


    public static ArrayList<Article> articleMaker(ArrayList<String> urls){
        try {
            ArrayList<Article> articles = new ArrayList<>();
            Document doc;
            for (String s : urls) {
                doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36").get();
                Elements el = doc.getElementsByAttribute("meta");
                for(Element e: el){
                    e.text();
                }
            }


            return articles;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[]args){
        try {
            SeekingAlphaParser.articleMaker(SeekingAlphaParser.articleFinder("IBM"));
        }catch (Exception e){

        }
    }
}
