package database;

//SETTINGS
//FAVORITES

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class Database {
    static FileOutputStream fos;
    static DataOutputStream outStream;
    static FileInputStream fis;
    static DataInputStream reader;

    public static void writeObject(JSONObject data) throws IOException{
        String url = "/home/james-aric/Documents/Github/frontend/src/main/java/database/saved/" + data.get("filename");
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            fos = new FileOutputStream(url);
            outStream = new DataOutputStream(new BufferedOutputStream(fos));
            System.out.println(data.toString());
            outStream.writeUTF(data.toString());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            outStream.close();
            fos.close();
        }
    }



    public static void writeArray(JSONArray data, String name) throws IOException{
        String url = "/home/james-aric/Documents/Github/frontend/src/main/java/database/saved/" + name;
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            fos = new FileOutputStream(url);
            outStream = new DataOutputStream(new BufferedOutputStream(fos));
            System.out.println(data.toString());
            outStream.writeUTF(data.toString());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            outStream.close();
            fos.close();
        }
    }



    public static JSONObject readObject(String filename) throws IOException{
        String result;
        try {
            fis = new FileInputStream("/home/james-aric/Documents/Github/frontend/src/main/java/database/saved/"+filename);
            reader = new DataInputStream(fis);
            result = reader.readUTF();
            reader.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }finally {
            reader.close();
            fis.close();
        }
        return new JSONObject(result);
    }

    public static JSONArray readArray(String filename) throws IOException{
        String result;
        try {
            fis = new FileInputStream("/home/james-aric/Documents/Github/frontend/src/main/java/database/saved/"+filename);
            reader = new DataInputStream(fis);
            result = reader.readUTF();
            reader.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }finally {
            reader.close();
            fis.close();
        }
        return new JSONArray(result);
    }


    public static void main(String[]args){
        JSONArray test = new JSONArray("[{\"date\":\"2018-09-07\",\"open\":221.85,\"high\":225.37,\"low\":220.71,\"close\":221.3,\"volume\":37619810,\"unadjustedVolume\":37619810,\"change\":-1.8,\"changePercent\":-0.807,\"vwap\":223.2009,\"label\":\"Sep 7\",\"changeOverTime\":0},{\"date\":\"2018-09-10\",\"open\":220.95,\"high\":221.85,\"low\":216.47,\"close\":218.33,\"volume\":39516453,\"unadjustedVolume\":39516453,\"change\":-2.97,\"changePercent\":-1.342,\"vwap\":218.23,\"label\":\"Sep 10\",\"changeOverTime\":-0.013420695887934923},{\"date\":\"2018-09-11\",\"open\":218.01,\"high\":224.299,\"low\":216.56,\"close\":223.85,\"volume\":35749049,\"unadjustedVolume\":35749049,\"change\":5.52,\"changePercent\":2.528,\"vwap\":221.8907,\"label\":\"Sep 11\",\"changeOverTime\":0.011522819701762235},{\"date\":\"2018-09-12\",\"open\":224.94,\"high\":225,\"low\":219.84,\"close\":221.07,\"volume\":49278740,\"unadjustedVolume\":49278740,\"change\":-2.78,\"changePercent\":-1.242,\"vwap\":221.7056,\"label\":\"Sep 12\",\"changeOverTime\":-0.0010393131495708007},{\"date\":\"2018-09-13\",\"open\":223.52,\"high\":228.35,\"low\":222.57,\"close\":226.41,\"volume\":41706377,\"unadjustedVolume\":41706377,\"change\":5.34,\"changePercent\":2.416,\"vwap\":225.9325,\"label\":\"Sep 13\",\"changeOverTime\":0.023090826931766763},{\"date\":\"2018-09-14\",\"open\":225.75,\"high\":226.84,\"low\":222.522,\"close\":223.84,\"volume\":31999289,\"unadjustedVolume\":31999289,\"change\":-2.57,\"changePercent\":-1.135,\"vwap\":224.319,\"label\":\"Sep 14\",\"changeOverTime\":0.011477632173520071},{\"date\":\"2018-09-17\",\"open\":222.15,\"high\":222.95,\"low\":217.27,\"close\":217.88,\"volume\":37195133,\"unadjustedVolume\":37195133,\"change\":-5.96,\"changePercent\":-2.663,\"vwap\":219.6097,\"label\":\"Sep 17\",\"changeOverTime\":-0.015454134658834233},{\"date\":\"2018-09-18\",\"open\":217.79,\"high\":221.85,\"low\":217.12,\"close\":218.24,\"volume\":31571712,\"unadjustedVolume\":31571712,\"change\":0.36,\"changePercent\":0.165,\"vwap\":219.4558,\"label\":\"Sep 18\",\"changeOverTime\":-0.013827383642114785},{\"date\":\"2018-09-19\",\"open\":218.5,\"high\":219.62,\"low\":215.3,\"close\":218.37,\"volume\":27123833,\"unadjustedVolume\":27123833,\"change\":0.13,\"changePercent\":0.06,\"vwap\":217.4068,\"label\":\"Sep 19\",\"changeOverTime\":-0.01323994577496614},{\"date\":\"2018-09-20\",\"open\":220.24,\"high\":222.28,\"low\":219.15,\"close\":220.03,\"volume\":26608794,\"unadjustedVolume\":26608794,\"change\":1.66,\"changePercent\":0.76,\"vwap\":220.7675,\"label\":\"Sep 20\",\"changeOverTime\":-0.0057388160867601},{\"date\":\"2018-09-21\",\"open\":220.78,\"high\":221.36,\"low\":217.29,\"close\":217.66,\"volume\":96246748,\"unadjustedVolume\":96246748,\"change\":-2.37,\"changePercent\":-1.077,\"vwap\":218.4944,\"label\":\"Sep 21\",\"changeOverTime\":-0.01644826028016274},{\"date\":\"2018-09-24\",\"open\":216.82,\"high\":221.26,\"low\":216.63,\"close\":220.79,\"volume\":27693358,\"unadjustedVolume\":27693358,\"change\":3.13,\"changePercent\":1.438,\"vwap\":219.4487,\"label\":\"Sep 24\",\"changeOverTime\":-0.00230456394035255},{\"date\":\"2018-09-25\",\"open\":219.75,\"high\":222.82,\"low\":219.7,\"close\":222.19,\"volume\":24554379,\"unadjustedVolume\":24554379,\"change\":1.4,\"changePercent\":0.634,\"vwap\":221.6295,\"label\":\"Sep 25\",\"changeOverTime\":0.004021690013556197},{\"date\":\"2018-09-26\",\"open\":221,\"high\":223.75,\"low\":219.76,\"close\":220.42,\"volume\":23984706,\"unadjustedVolume\":23984706,\"change\":-1.77,\"changePercent\":-0.797,\"vwap\":221.8912,\"label\":\"Sep 26\",\"changeOverTime\":-0.003976502485314161},{\"date\":\"2018-09-27\",\"open\":223.82,\"high\":226.44,\"low\":223.54,\"close\":224.95,\"volume\":30181227,\"unadjustedVolume\":30181227,\"change\":4.53,\"changePercent\":2.055,\"vwap\":225.1658,\"label\":\"Sep 27\",\"changeOverTime\":0.016493447808404775},{\"date\":\"2018-09-28\",\"open\":224.79,\"high\":225.84,\"low\":224.02,\"close\":225.74,\"volume\":22929364,\"unadjustedVolume\":22929364,\"change\":0.79,\"changePercent\":0.351,\"vwap\":225.1516,\"label\":\"Sep 28\",\"changeOverTime\":0.020063262539539075},{\"date\":\"2018-10-01\",\"open\":227.95,\"high\":229.42,\"low\":226.35,\"close\":227.26,\"volume\":23600802,\"unadjustedVolume\":23600802,\"change\":1.52,\"changePercent\":0.673,\"vwap\":228.0504,\"label\":\"Oct 1\",\"changeOverTime\":0.026931766832354178},{\"date\":\"2018-10-02\",\"open\":227.25,\"high\":230,\"low\":226.63,\"close\":229.28,\"volume\":24788170,\"unadjustedVolume\":24788170,\"change\":2.02,\"changePercent\":0.889,\"vwap\":229.0433,\"label\":\"Oct 2\",\"changeOverTime\":0.036059647537279665},{\"date\":\"2018-10-03\",\"open\":230.05,\"high\":233.47,\"low\":229.78,\"close\":232.07,\"volume\":28654799,\"unadjustedVolume\":28654799,\"change\":2.79,\"changePercent\":1.217,\"vwap\":232.2344,\"label\":\"Oct 3\",\"changeOverTime\":0.04866696791685486},{\"date\":\"2018-10-04\",\"open\":230.78,\"high\":232.35,\"low\":226.73,\"close\":227.99,\"volume\":32042000,\"unadjustedVolume\":32042000,\"change\":-4.08,\"changePercent\":-1.758,\"vwap\":228.7941,\"label\":\"Oct 4\",\"changeOverTime\":0.030230456394035234},{\"date\":\"2018-10-05\",\"open\":227.96,\"high\":228.41,\"low\":220.58,\"close\":224.29,\"volume\":33580463,\"unadjustedVolume\":33580463,\"change\":-3.7,\"changePercent\":-1.623,\"vwap\":224.3801,\"label\":\"Oct 5\",\"changeOverTime\":0.013511070944419253}]");
        //test.put("filename", "aapl");
        try {
            Database.writeArray(test, "aapl");
            //System.out.println(Database.read("settings.txt").toString());

            test = Database.readArray("aapl");
            System.out.println(test.toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
