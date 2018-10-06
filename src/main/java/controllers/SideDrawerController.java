package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class SideDrawerController implements Initializable {

    @FXML
    private JFXListView listView;

    @FXML
    public VBox dashboardDrawerVBox;

    ObservableList<String> list = FXCollections.observableArrayList("Test 1","Test 2","Test 3", "Test 4");

    @Override
    public void initialize(URL url, ResourceBundle rb){
        listView.setItems(list);
        //listView.setExpanded(true);
        //listView.setVerticalGap(20.0);
    }

    public void expandListView(){
        listView.setExpanded(true);
        listView.setVerticalGap(20.0);
    }

    public void shrinkListView(){
        listView.setExpanded(false);
        listView.setVerticalGap(0.0);
    }
}
