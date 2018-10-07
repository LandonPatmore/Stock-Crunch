package database;

//SETTINGS
//FAVORITES

import org.json.JSONObject;

import java.io.*;

public class Database {
    static FileOutputStream fos;
    static DataOutputStream outStream;
    static FileInputStream fis;
    static DataInputStream reader;

    public static void write(JSONObject data) throws IOException{
        try {
            File f = new File("~/Desktop/database/");
            if (f.exists()) {
                f.delete();
            }
            fos = new FileOutputStream(data.getString("filename"));
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

    public static JSONObject read(String filename) throws IOException{
        String result;
        try {
            fis = new FileInputStream(filename);
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


    /*public static void main(String[]args){
        JSONObject test = new JSONObject();
        test.put("filename", "settings");
        test.put("body", "DWDbUAWHGHJAJKAWBDJWABHJAWBDAWBDIHWABDJ");
        try {
            Database.write(test);
            System.out.println(Database.read("settings.txt").toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }*/
}
