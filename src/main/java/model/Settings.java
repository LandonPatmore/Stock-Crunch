package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    private static final Properties properties = new Properties();
    private static final InputStream stream = Settings.class.getResourceAsStream("/settings.properties");

    /**
     * Loads the settings file
     * @return if the file was loaded or not
     */
    public static boolean loadSettings(){
        try {
            properties.load(stream);
            return true;
        } catch (IOException e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Saves the settings to the settings file
     * @return if the file was saved or not
     */
    public static boolean saveSettings(){
        try {
            properties.store(new FileWriter(Settings.class.getResource("/settings.properties").getPath()), null);
            return true;
        } catch (IOException e){
            System.out.println(e.getLocalizedMessage());
            // TODO: Have properties set to default
            return false;
        }
    }

    /**
     * Sets the theme
     * @param theme theme name
     */
    public static void setTheme(String theme){
        properties.setProperty("theme", theme);
        saveSettings();
    }

    /**
     * Sets the interval stock updates are made
     * @param interval interval time
     */
    public static void setStockUpdateInterval(String interval){
        properties.setProperty("stock_update_interval", interval);
        saveSettings();
    }

    /**
     * Sets the interval article updates are made
     * @param interval interval time
     */
    public static void setArticleUpdateInterval(String interval){
        properties.setProperty("article_update_interval", interval);
        saveSettings();
    }

    /**
     * Gets the theme
     * @return name of the theme
     */
    public static String getTheme(){
        return properties.getProperty("theme");
    }

    /**
     * Gets the stock update interval
     * @return stock update interval
     */
    public static String getStockUpdateInterval(){
        return properties.getProperty("stock_update_interval");
    }

    /**
     * Gets the article update interval
     * @return article update interval
     */
    public static String getArticleUpdateInterval(){
        return properties.getProperty("article_update_interval");
    }

}
