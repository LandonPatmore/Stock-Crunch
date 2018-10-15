package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import dataobjects.Favorites;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Log;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.net.URL;
import java.util.ResourceBundle;

public class SideDrawerController implements Initializable {
    private static final Log logger = new Log(SideDrawerController.class);

    @FXML
    public JFXListView listView;

    @FXML
    public VBox dashboardDrawerVBox;

    @FXML
    public Pane spacerPane;

    @FXML
    private JFXTextField stockSearchFieldForFavs;

    public ObservableList<String> list = Favorites.readData() != null ? Favorites.readData() : FXCollections.observableArrayList();
    private boolean isValidTicker = false;
    private static String selectedStock = "";
    public static Boolean isBullish = true;


    public static String getSelectedStock() {
        return selectedStock;
    }

    @FXML
    public void addToFavorites(ActionEvent event) {
        if (validTicker(stockSearchFieldForFavs.getText().toUpperCase())) {
            list.add(stockSearchFieldForFavs.getText().toUpperCase());
            stockSearchFieldForFavs.clear();
            Platform.runLater(() -> Favorites.serializeData(list));
        } else {
            HomeController.setInValidTickerInFavorites(true);
            stockSearchFieldForFavs.clear();
            HomeController.setInValidTickerInFavorites(false);
        }
    }

    @FXML
    public static void openFavorite(ActionEvent event){
        selectedStock = ((Control)event.getSource()).getId();
        HomeController.setLoadStockGraph();
        if(isBullish(selectedStock)){
            HomeController.setThemeBullish(true);
        }else{
            HomeController.setThemeBearish(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setItems(list);
        listView.setCellFactory(param -> new deletableCell());
        //listView.setExpanded(true);
        //listView.setVerticalGap(20.0);
        logger.info("SideDrawerController has been initialized.",false);
    }

    static class deletableCell extends ListCell<String> {
        HBox hbox = new HBox();
        JFXButton text = new JFXButton("");
        FontAwesomeIconView timesIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES);
        private static BooleanProperty openedSideDrawer = new SimpleBooleanProperty(false);
        private String bullish = getClass().getResource("/favorites-bullish.css").toExternalForm();
        private String bearish = getClass().getResource("/favorites-bearish.css").toExternalForm();

        public static void setOpenedSideDrawer(Boolean x) {
            openedSideDrawer.setValue(x);
        }

        public deletableCell() {
            super();
            timesIcon.setStyle("-fx-fill: #7f8fa6");
            timesIcon.setSize("16px");
            hbox.getChildren().addAll(text, timesIcon);
            hbox.setAlignment(Pos.CENTER);
            text.setAlignment(Pos.CENTER_LEFT);
            text.setMaxWidth(163);
            text.setPrefWidth(163);
            text.setOnAction(e ->openFavorite(e));
            //text.getStylesheets().add(bearish);
            timesIcon.setOnMouseClicked(MouseEvent -> {
                Platform.runLater(() -> {
                    getListView().getItems().remove(getItem());
                    Favorites.serializeData(getListView().getItems());
                });
            });


            openedSideDrawer.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        Task task = new Task<Void>() {
                            @Override
                            public Void call() {
                                if (SideDrawerController.isBullish(text.getText())) {
                                    text.getStylesheets().clear();
                                    text.getStylesheets().add(bullish);
                                    isBullish = true;
                                } else {
                                    text.getStylesheets().clear();
                                    text.getStylesheets().add(bearish);
                                    isBullish = false;
                                }
                                return null;
                            }
                        };
                        new Thread(task).start();
                    }
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                text.setText(item);
                text.setId(item);
                setGraphic(hbox);
                if (SideDrawerController.isBullish(text.getText())) {
                    text.getStylesheets().clear();
                    text.getStylesheets().add(bullish);
                } else {
                    text.getStylesheets().clear();
                    text.getStylesheets().add(bearish);
                }
            }
        }
    }

    private boolean validTicker(String ticker) {
        try {
            Jsoup.connect("https://api.iextrading.com/1.0/stock/" + ticker + "/quote").ignoreContentType(true).get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBullish(String ticker) {
        if (!ticker.equals("")) {
            try {
                Document document = Jsoup.connect("https://api.iextrading.com/1.0/stock/" + ticker + "/quote").ignoreContentType(true).get();
                JSONObject json = new JSONObject(document.text());
                if (Double.parseDouble(json.get("change").toString()) < 0) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                System.out.println("Oopsy Whoospie i mawde a ewwor oWo @ isBullish  " + e.getLocalizedMessage());
                return false;
            }
        }
        return false;
    }

}
