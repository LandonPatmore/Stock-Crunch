package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import dataobjects.Article;
import dataobjects.NasdaqArticleRSSFeed;
import dataobjects.RSSFeedProvider;
import dataworkers.DataFetcher;
import dataworkers.RSSFeedFetcher;
import dataworkers.StockFetcher;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Settings;
import parsers.NasdaqArticleParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    @FXML
    LineChart linechart;

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
    private JFXButton searchButton = new JFXButton();

    public static void setSetThemDarkTrue(){
        setThemeDark.setValue(true);
        Settings.setTheme("dark");
    }

    public static void setSetThemLightTrue(){
        setThemeLight.setValue(true);
        Settings.setTheme("light");
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

        setThemeDark.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    currentTheme = darkBullish;
                    clearStyleSheets();
                    css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                    addStyleSheets();
                    setThemeLight.setValue(false);
                }
            }
        });

        setThemeLight.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    currentTheme = lightBearish;
                    clearStyleSheets();
                    css = this.getClass().getClassLoader().getResource(currentTheme).toExternalForm();
                    addStyleSheets();
                    setThemeDark.setValue(false);
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

        final Button b = new Button("Bring up article");
        b.setOnAction(event -> {
            final Article a = RSSFeedFetcher.grabArticles(RSSFeedProvider.NASDAQ, RSSFeedProvider.NASDAQ_RSS_FEED, NasdaqArticleRSSFeed.SYMBOL.getValue() + "mcd").get(0);
            NasdaqArticleParser.getArticleData(a);
            System.out.println(a.getBody().toString());
            createArticleViewer(a);
        });


        stockSearchField = new JFXTextField();
        stockSearchField.setPromptText("Search for a Stock...");
        stockSearchField.setLabelFloat(false);
        stockSearchField.setPrefSize(300,20);
        stockSearchField.setMaxSize(300,20);
        String searchCss = this.getClass().getClassLoader().getResource("stock-search-style.css").toExternalForm();
        stockSearchField.getStylesheets().add(searchCss);
        StackPane.setAlignment(stockSearchField, Pos.CENTER);
        searchButton = new JFXButton();
        stocksInfoPane.getChildren().addAll(stockSearchField, b);
        scrollPaneForStockPane.setContent(stocksInfoPane);
        scrollPaneForStockPane.setPannable(true);
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

        ((NumberAxis) linechart.getYAxis()).setForceZeroInRange(false);
        linechart.setVisible(false);

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

    private void createArticleViewer(Article article){
        final ScrollPane scrollPane = new ScrollPane();
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        scrollPane.setContent(browser);

        webEngine.loadContent(article.getBody().toString());

        mainSplitPane.getItems().add(scrollPane);
    }

    private void loadGraph(String url, int num, String timeframe, int totalNum){
        linechart.getData().add(GraphController.getGraphData(StockFetcher.stockDataHistorical(url, num, timeframe), totalNum));
        linechart.setVisible(true);
    }
}
