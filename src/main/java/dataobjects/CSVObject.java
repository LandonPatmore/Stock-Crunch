package dataobjects;

import java.util.ArrayList;

public class CSVObject {

    private final String columnName;
    private final ArrayList<String> data;

    public CSVObject(String columnName, ArrayList<String> data) {
        this.columnName = columnName;
        this.data = data;
    }

    public String getColumnName() {
        return columnName;
    }

    public ArrayList<String> getData() {
        return data;
    }
}
