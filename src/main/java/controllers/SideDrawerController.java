package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.net.URL;
import java.util.ResourceBundle;

public class SideDrawerController implements Initializable {

    @FXML
    public JFXListView listView;

    @FXML
    public VBox dashboardDrawerVBox;

    @FXML
    public Pane spacerPane;

    @FXML
    private JFXTextField stockSearchFieldForFavs;

    public ObservableList<String> list = FXCollections.observableArrayList("TSLA", "AAPL", "MSFT", "NVDA", "AMD");
    private boolean isValidTicker = false;

    @FXML
    public void addToFavorites(ActionEvent event){
        if (validTicker(stockSearchFieldForFavs.getText().toUpperCase())){
            list.add(stockSearchFieldForFavs.getText().toUpperCase());
            stockSearchFieldForFavs.clear();
        } else{
            HomeController.setInValidTickerInFavorites(true);
            stockSearchFieldForFavs.clear();
            HomeController.setInValidTickerInFavorites(false);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb){
        listView.setItems(list);
        listView.setCellFactory(param -> new deletableCell());
        //listView.setExpanded(true);
        //listView.setVerticalGap(20.0);
    }

    static class deletableCell extends ListCell<String> {
        HBox hbox = new HBox();
        JFXButton text = new JFXButton( "");
        //Button button = new Button("Del");
        FontAwesomeIconView thumbsDownIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES);

        public deletableCell() {
            super();
            thumbsDownIcon.setStyle("-fx-fill: #7f8fa6");
            thumbsDownIcon.setSize("16px");
            hbox.getChildren().addAll(text, thumbsDownIcon);
            hbox.setAlignment(Pos.CENTER);
            text.setAlignment(Pos.CENTER_LEFT);
            text.setMaxWidth(163);
            text.setPrefWidth(163);
            thumbsDownIcon.setOnMouseClicked(MouseEvent -> getListView().getItems().remove(getItem()));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                text.setText(item);
                setGraphic(hbox);
            }
        }
    }

    public void expandListView(){
        listView.setExpanded(true);
        listView.setVerticalGap(20.0);
    }

    public void shrinkListView(){
        listView.setExpanded(false);
        listView.setVerticalGap(0.0);
    }

    private boolean validTicker(String ticker){
        try{
            Jsoup.connect("https://api.iextrading.com/1.0/stock/" + ticker + "/quote").ignoreContentType(true).get();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isBullish(String ticker){
        try{
            Document document = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + ticker + "/quote").ignoreContentType(true).get();
            JSONObject json = new JSONObject(document.text());
            if(Double.parseDouble(json.get("change").toString()) < 0){
                return false;
            }
            return true;
        }catch (Exception e){
            System.out.println("Oopsy Whoospie i mawde a ewwor oWo @ isBullish  " + e.getLocalizedMessage());
            return false;
        }
    }

}
