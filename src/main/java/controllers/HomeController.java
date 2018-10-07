package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import dataobjects.Article;
import dataworkers.StockFetcher;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.Text;
import model.Settings;
import org.json.JSONObject;
import parsers.NasdaqArticleParser;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {

    @FXML
    AnchorPane anchorPane;

    @FXML
    JFXHamburger hamburger;

    @FXML
    SplitPane mainSplitPane;

    @FXML
    JFXButton settingsButton;

    @FXML
    GridPane topBarGridPane;

    @FXML
    JFXDrawer settingsDrawer;

    @FXML
    JFXDrawer sideDrawer;

    @FXML
    ColumnConstraints gridPaneLeft;

    @FXML
    ColumnConstraints gridPaneCenter;

    @FXML
    ColumnConstraints gridPaneRight;


    Label current = new Label();
    Label open = new Label();
    Label close = new Label();
    Label volume = new Label();
    Label high = new Label();
    Label low = new Label();
    Label change = new Label();
    HBox stats = new HBox();
    HBox headers = new HBox();

    private boolean articleIsOpen = false;
    private SideDrawerController sideDrawerController;
    private SettingsDrawerController settingsDrawerController;
    private String darkBullish = "bullish-dark.css";
    private String lightBullish = "bullish-light.css";
    private String darkBearish = "bearish-dark.css";
    private String lightBearish = "bearish-light.css";
    private String currentTheme = darkBullish;
    private String css;
    private static BooleanProperty setThemeDark = new SimpleBooleanProperty(false);
    private static BooleanProperty setThemeLight = new SimpleBooleanProperty(false);
    private static BooleanProperty setThemeBullish = new SimpleBooleanProperty(false);
    private static BooleanProperty setThemeBearish = new SimpleBooleanProperty(false);
    private JFXTextField stockSearchField;
    private ScrollPane scrollPaneForStockPane = new ScrollPane();
    private StackPane stocksInfoPane = new StackPane();
    private JFXButton searchButton;
    private static BooleanProperty inValidTickerInFavorites = new SimpleBooleanProperty(false);
    private static BooleanProperty loadStockGraph = new SimpleBooleanProperty(false);
    private LineChart linechart;
    private VBox stockPaneVBox;
    private Label ticker;
    private static boolean isBullish;
    private static boolean isDarkMode;


    public static void setSetThemDarkTrue(){
        setThemeDark.setValue(true);
        Settings.setTheme("dark");
    }

    public static void setSetThemLightTrue(){
        setThemeLight.setValue(true);
        Settings.setTheme("light");
    }

    public static void setInValidTickerInFavorites(Boolean x){
        inValidTickerInFavorites.setValue(x);
    }

    public static void setLoadStockGraph(){
        loadStockGraph.setValue(true);
    }

    public static void setSetThemeBullish(Boolean x){
        setThemeBullish.setValue(x);
    }
    public static void setSetThemeBearish(Boolean x){
        setThemeBearish.setValue(x);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Settings.loadSettings();

        //Load side Drawer FXML
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Side_Drawer.fxml"));
            VBox box = loader.load();
            sideDrawerController = loader.getController();
            sideDrawerController.dashboardDrawerVBox.maxHeightProperty().bind(anchorPane.heightProperty());
            sideDrawer.setSidePane(box);
        } catch (IOException ex) {
            Logger.getLogger(SideDrawerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Load settings top drawer
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Settings_Drawer.fxml"));
            AnchorPane ap = loader.load();
            settingsDrawerController = loader.getController();
            settingsDrawerController.settingsPane.maxWidthProperty().bind(anchorPane.widthProperty());
            settingsDrawer.setSidePane(ap);
        } catch (IOException ex) {
            Logger.getLogger(SettingsDrawerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
        addStyleSheets();

        setThemeBullish.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isBullish = true;
                    if(isDarkMode){
                        currentTheme = darkBullish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeLight.setValue(false);
                        setSetThemeBearish(false);
                    }else{
                        currentTheme = lightBullish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeDark.setValue(false);
                        setSetThemeBearish(false);
                    }
                }
            }
        });

        setThemeBearish.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isBullish = false;
                    if(isDarkMode){
                        currentTheme = darkBearish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeLight.setValue(false);
                        setSetThemeBullish(false);
                    } else {
                        currentTheme = lightBearish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeDark.setValue(false);
                        setSetThemeBullish(false);
                    }
                }
            }
        });

        setThemeDark.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isDarkMode = true;
                    if(isBullish) {
                        currentTheme = darkBullish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeLight.setValue(false);
                    }else{
                        currentTheme = darkBearish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeLight.setValue(false);
                    }
                }
            }
        });

        setThemeLight.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isDarkMode = false;
                    if(isBullish) {
                        currentTheme = lightBullish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeDark.setValue(false);
                    }else{
                        currentTheme = lightBearish;
                        clearStyleSheets();
                        css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                        addStyleSheets();
                        setThemeDark.setValue(false);
                    }
                }
            }
        });

        //add id to stylesheet
        anchorPane.setId("anchorpane");
        mainSplitPane.setId("split");
        topBarGridPane.setId("topBar");
        stocksInfoPane.setId("stocksInfoPane");
        scrollPaneForStockPane.setId("scrollPaneForStockPane");
        sideDrawerController.dashboardDrawerVBox.setId("sidedrawervbox");
        sideDrawerController.listView.setId("sidedrawerlistview");
        sideDrawerController.spacerPane.setId("spacerpane");
        settingsDrawerController.settingsPane.setId("settingsdrawerpane");

        //Make stuff resize right
        topBarGridPane.prefWidthProperty().bind(anchorPane.widthProperty());
        mainSplitPane.prefWidthProperty().bind(anchorPane.widthProperty());
        mainSplitPane.prefHeightProperty().bind(anchorPane.heightProperty());
        gridPaneLeft.maxWidthProperty().bind(topBarGridPane.widthProperty());
        gridPaneRight.maxWidthProperty().bind(topBarGridPane.widthProperty());
        sideDrawer.prefHeightProperty().bind(anchorPane.heightProperty());
        settingsDrawer.prefWidthProperty().bind(anchorPane.widthProperty());

        Label temp = new Label();
        temp.setText("OPEN");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);
        temp = new Label();
        temp.setText("CLOSE");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);
        temp = new Label();
        temp.setText("VOLUME");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);
        temp = new Label();
        temp.setText("CURRENT");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);
        temp = new Label();
        temp.setText("CHANGE");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);
        temp = new Label();
        temp.setText("LOW");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);
        temp = new Label();
        temp.setText("HIGH");
        temp.setTextFill(Paint.valueOf("white"));
        headers.getChildren().add(temp);

        headers.setSpacing(10);
        headers.setAlignment(Pos.CENTER);

        sideDrawer.setVisible(false);
        settingsDrawer.setVisible(false);
        //Allows you to click through the drawer if it's not visible (so we set it invisible when it's not open)
        sideDrawer.setPickOnBounds(false);
        settingsDrawer.setPickOnBounds(false);


        HamburgerBasicCloseTransition basicCloseTransition = new HamburgerBasicCloseTransition(hamburger);
        basicCloseTransition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            basicCloseTransition.setRate(basicCloseTransition.getRate() * -1);
            basicCloseTransition.play();
            if (sideDrawer.isOpened()) {
                //sideDrawerController.shrinkListView();
                sideDrawer.close();
                hamburger.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        SideDrawerController.deletableCell.setOpenedSideDrawer(false);
                    }
                });
                //This starts a Thread but immediately schedules it to run after 500 milliseconds, so the drawer closing animation can run before making the drawer invisible
                //Thanks for writing this Doug Lea
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
                executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        sideDrawer.setVisible(false);
                        hamburger.setDisable(false);
                    }
                }, 500, TimeUnit.MILLISECONDS);
            } else {
                sideDrawer.setVisible(true);
                sideDrawer.open();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        SideDrawerController.deletableCell.setOpenedSideDrawer(true);
                    }
                });
                //sideDrawerController.expandListView();
            }
        });

        settingsButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (settingsDrawer.isOpened()) {
                Settings.loadSettings();
                settingsDrawer.close();
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
                executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        settingsDrawer.setVisible(false);
                    }
                }, 500, TimeUnit.MILLISECONDS);
            } else {
                settingsDrawer.setVisible(true);
                settingsDrawer.open();
            }
        });

        JFXListView<Label> list = new JFXListView<>();
        for (int i = 0; i < 100; i++) {
            list.getItems().add(new Label("Item " + i));
        }
        list.getStyleClass().add("mylistview");
        list.setMaxHeight(3400);


        stockPaneVBox = new VBox();
        stockPaneVBox.setSpacing(10);
        stockPaneVBox.setAlignment(Pos.CENTER);
        StackPane.setAlignment(stockPaneVBox, Pos.CENTER);
        stocksInfoPane.getChildren().addAll(stockPaneVBox);
        scrollPaneForStockPane.setContent(stocksInfoPane);
        stocksInfoPane.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPaneForStockPane.getViewportBounds().getWidth(), scrollPaneForStockPane.viewportBoundsProperty()));
        stocksInfoPane.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPaneForStockPane.getViewportBounds().getHeight(), scrollPaneForStockPane.viewportBoundsProperty()));

        mainSplitPane.getItems().add(scrollPaneForStockPane);

        if("dark".equals(Settings.getTheme())){
            setThemeDark.setValue(true);
        } else {
            setThemeLight.setValue(true);
        }

        //((NumberAxis) linechart.getYAxis()).setForceZeroInRange(false);
        //linechart.setVisible(false);
        inValidTickerInFavorites.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    JFXDialogLayout content = new JFXDialogLayout();
                    content.setHeading(new Text("Invalid Ticker!"));
                    content.setBody(new Text("No stock was found under that ticker, please try again."));
                    JFXDialog wrongInfo = new JFXDialog(stocksInfoPane,content, JFXDialog.DialogTransition.LEFT);
                    JFXButton button =  new JFXButton("Okay");
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            wrongInfo.close();
                        }
                    });
                    content.setActions(button);
                    wrongInfo.show();
                }
            }
        });

        loadStockGraph.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (!SideDrawerController.getSelectedStock().equals("")) {
                        String selectedStock = SideDrawerController.getSelectedStock();
                        if(linechart == null) {
                            CategoryAxis xAxis = new CategoryAxis();
                            NumberAxis yAxis = new NumberAxis();
                            yAxis.setForceZeroInRange(false);
                            linechart = new LineChart(xAxis, yAxis);
                            linechart.setTitle(selectedStock);
                            linechart.setCreateSymbols(false);
                            linechart.setMaxSize(600,450);
                            linechart.setLegendVisible(false);
                            stockPaneVBox.getChildren().addAll(linechart);
                            stockPaneVBox.setVgrow(linechart,Priority.ALWAYS);
                            linechart.setLegendVisible(false);
                            stocksInfoPane.getChildren().addAll(linechart);
                            stockPaneVBox.getChildren().add(linechart);
                        }
                        else {
                            linechart.getData().removeAll(Collections.singleton(linechart.getData().setAll()));
                        }
                        linechart.setTitle(selectedStock);
                        String graph = this.getClass().getClassLoader().getResource("graph-bullish.css").toExternalForm();
                        linechart.getStylesheets().add(graph);
                        loadGraph(selectedStock,1,"d",100);
                        //if(!stocksInfoPane.getChildren().contains(linechart));
                        if(!stockPaneVBox.getChildren().contains(headers)){
                            stockPaneVBox.getChildren().add(headers);
                        }

                        if(!stockPaneVBox.getChildren().contains(stats)){
                            stockPaneVBox.getChildren().add(stats);
                            loadData(selectedStock);
                            stats.setSpacing(15);
                            stats.setAlignment(Pos.CENTER);
                            stats.getChildren().add(open);
                            stats.getChildren().add(close);
                            stats.getChildren().add(volume);
                            stats.getChildren().add(current);
                            stats.getChildren().add(change);
                            stats.getChildren().add(low);
                            stats.getChildren().add(high);
                        }
                        else{
                            loadData(selectedStock);
                        }
                        loadStockGraph.setValue(false);
                    }
                }
            }
        });
    }

    private void addStyleSheets() {
        anchorPane.getStylesheets().add(css);
        mainSplitPane.getStylesheets().add(css);
        topBarGridPane.getStylesheets().add(css);
        settingsButton.getStylesheets().add(css);
        stocksInfoPane.getStylesheets().add(css);
        settingsDrawer.getStylesheets().add(css);
        scrollPaneForStockPane.getStylesheets().add(css);
        sideDrawerController.dashboardDrawerVBox.getStylesheets().add(css);
        sideDrawerController.listView.getStylesheets().add(css);
        sideDrawerController.spacerPane.getStylesheets().add(css);
        settingsDrawerController.settingsPane.getStylesheets().add(css);
        settingsDrawerController.lightToggle.getStylesheets().add(css);
        settingsDrawerController.darkToggle.getStylesheets().add(css);
    }

    private void clearStyleSheets(){
        anchorPane.getStylesheets().clear();
        mainSplitPane.getStylesheets().clear();
        topBarGridPane.getStylesheets().clear();
        settingsButton.getStylesheets().clear();
        stocksInfoPane.getStylesheets().clear();
        settingsDrawer.getStylesheets().clear();
        scrollPaneForStockPane.getStylesheets().clear();
        sideDrawerController.dashboardDrawerVBox.getStylesheets().clear();
        sideDrawerController.listView.getStylesheets().clear();
        sideDrawerController.spacerPane.getStylesheets().clear();
        settingsDrawerController.settingsPane.getStylesheets().clear();
        settingsDrawerController.lightToggle.getStylesheets().clear();
        settingsDrawerController.darkToggle.getStylesheets().clear();
    }

    private void createArticleViewer(Article article) {
        if (!articleIsOpen) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() {
                    final ScrollPane scrollPane = new ScrollPane();
                    final VBox container = new VBox();
                    final WebView browser = new WebView();
                    final WebEngine webEngine = browser.getEngine();

                    final Button closeButton = new Button("Close");

                    closeButton.setOnAction(event -> {
                        mainSplitPane.getItems().remove(container);
                        articleIsOpen = false;
                    });

                    scrollPane.setContent(browser);

                    container.getChildren().addAll(closeButton, scrollPane);

                    webEngine.loadContent(article.getBody().toString());
                    webEngine.setUserStyleSheetLocation(getClass().getResource("/" + currentTheme).toString());

                    mainSplitPane.getItems().add(container);
                    articleIsOpen = true;

                    return null;
                }
            };

            new Thread(task).start();
        }
    }

    private void loadGraph(String url, int num, String timeframe, int totalNum){
        linechart.getData().add(GraphController.getGraphData(StockFetcher.stockDataHistorical(url, num, timeframe), totalNum));
        linechart.setVisible(true);
    }

    private void loadData(String url){
        JSONObject object = StockFetcher.stockDataCurrent(url);
        open.setText(object.get("open").toString());
        open.setTextFill(Paint.valueOf("white"));
        change.setText(object.get("change").toString());
        change.setTextFill(Paint.valueOf("white"));
        current.setText(object.get("latestPrice").toString());
        current.setTextFill(Paint.valueOf("white"));
        low.setText(object.get("low").toString());
        low.setTextFill(Paint.valueOf("white"));
        high.setText(object.get("high").toString());
        high.setTextFill(Paint.valueOf("white"));
        close.setText(object.get("close").toString());
        close.setTextFill(Paint.valueOf("white"));
        volume.setText(object.get("latestVolume").toString());
        volume.setTextFill(Paint.valueOf("white"));
    }
}
