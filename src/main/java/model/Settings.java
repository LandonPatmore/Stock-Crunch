package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    private static final Properties properties = new Properties();
    private static final InputStream stream = Settings.class.getResourceAsStream("/settings.properties");

    public static boolean loadSettings(){
        try {
            properties.load(stream);
            return true;
        } catch (IOException e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    // TODO: Probably should make this a boolean so we know if the settings were actually saved
    public static void saveSettings(){
        try {
            properties.store(new FileWriter(Settings.class.getResource("/settings.properties").getPath()), null);
        } catch (IOException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void setTheme(String theme){
        properties.setProperty("theme", theme);
        saveSettings();
    }

    public static void setStockUpdateInterval(String interval){
        properties.setProperty("stock_update_interval", interval);
        saveSettings();
    }

    public static void setArticleUpdateInterval(String interval){
        properties.setProperty("article_update_interval", interval);
        saveSettings();
    }

    public static String getTheme(){
        return properties.getProperty("theme");
    }

    public static String getStockUpdateInterval(){
        return properties.getProperty("stock_update_interval");
    }

    public static String getArticleUpdateInterval(){
        return properties.getProperty("article_update_interval");
    }

}
