package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import dataobjects.*;
import dataworkers.RSSFeedFetcher;
import dataworkers.SentimentAnalyzer;
import dataworkers.StockFetcher;
import javafx.application.HostServices;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.text.Text;
import model.Log;
import model.Settings;
import org.json.JSONObject;
import parsers.ArticleParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController implements Initializable {
    private static final Log logger = new Log(HomeController.class);

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


    private Label current = new Label();
    private Label open = new Label();
    private Label close = new Label();
    private Label volume = new Label();
    private Label high = new Label();
    private Label low = new Label();
    private Label change = new Label();
    private HBox stats = new HBox();
    private HBox headers = new HBox();
    private VBox links = new VBox();

    private boolean articleIsOpen = false;
    private SideDrawerController sideDrawerController;
    private SettingsDrawerController settingsDrawerController;
    private String darkBullish = "bullish-dark.css";
    private String lightBullish = "bullish-light.css";
    private String darkBearish = "bearish-dark.css";
    private String lightBearish = "bearish-light.css";
    private String currentTheme = darkBullish;
    private String css;
    private static BooleanProperty themeDark = new SimpleBooleanProperty(false);
    private static BooleanProperty themeLight = new SimpleBooleanProperty(false);
    private static BooleanProperty themeBullish = new SimpleBooleanProperty(false);
    private static BooleanProperty themeBearish = new SimpleBooleanProperty(false);
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

    static HostServices hs;

    public static void setEs(HostServices es) {
        hs = es;
    }


    public static void setSetThemDarkTrue() {
        themeDark.setValue(true);
        Settings.setTheme("dark");
    }

    public static void setSetThemLightTrue() {
        themeLight.setValue(true);
        Settings.setTheme("light");
    }

    public static void setInValidTickerInFavorites(Boolean x) {
        inValidTickerInFavorites.setValue(x);
    }

    public static void setLoadStockGraph() {
        loadStockGraph.setValue(true);
    }

    public static void setThemeBullish(Boolean x) {
        themeBullish.setValue(x);
    }

    public static void setThemeBearish(Boolean x) {
        themeBearish.setValue(x);
    }

    private void setThemeing(boolean bullish, boolean dark){
        clearStyleSheets();
        css = getClass().getResource("/" + currentTheme).toExternalForm();
        addStyleSheets();


        if(bullish){
            setThemeBullish(bullish);
            setThemeBearish(!bullish);
        } else {
            setThemeBullish(!bullish);
            setThemeBearish(bullish);
        }

        if(dark){
            setSetThemDarkTrue();
        } else {
            setSetThemLightTrue();
        }

        themeDark.setValue(false);
        setThemeBearish(false);
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

        css = getClass().getResource("/" + currentTheme).toExternalForm();
        addStyleSheets();

        themeBullish.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isBullish = true;
                    if (isDarkMode) {
                        currentTheme = darkBullish;
                    } else {
                        currentTheme = lightBullish;
                    }

                    clearStyleSheets();
                    css = getClass().getResource("/" + currentTheme).toExternalForm();
                    addStyleSheets();
                    themeDark.setValue(false);
                    setThemeBearish(false);
                }
            }
        });

        themeBearish.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isBullish = false;
                    if (isDarkMode) {
                        currentTheme = darkBearish;
                    } else {
                        currentTheme = lightBearish;
                    }

                    clearStyleSheets();
                    css = getClass().getResource("/" + currentTheme).toExternalForm();
                    addStyleSheets();
                    themeDark.setValue(false);
                    setThemeBullish(false);
                }
            }
        });

        themeDark.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isDarkMode = true;
                    if (isBullish) {
                        currentTheme = darkBullish;
                    } else {
                        currentTheme = darkBearish;
                    }

                    clearStyleSheets();
                    css = getClass().getResource("/" + currentTheme).toExternalForm();
                    addStyleSheets();
                    themeLight.setValue(false);
                }
            }
        });

        themeLight.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    isDarkMode = false;
                    if (isBullish) {
                        currentTheme = lightBullish;
                    } else {
                        currentTheme = lightBearish;
                    }

                    clearStyleSheets();
                    css = getClass().getResource("/" + currentTheme).toExternalForm();
                    addStyleSheets();
                    themeDark.setValue(false);
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
                    @Override
                    public void run() {
                        SideDrawerController.deletableCell.setOpenedSideDrawer(false);
                    }
                });
                //This starts a Thread but immediately schedules it to run after 500 milliseconds, so the drawer closing animation can run before making the drawer invisible
                //Thanks for writing this Doug Lea
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
                executor.schedule(() -> {
                    sideDrawer.setVisible(false);
                    hamburger.setDisable(false);
                }, 500, TimeUnit.MILLISECONDS);
            } else {
                sideDrawer.setVisible(true);
                sideDrawer.open();
                Platform.runLater(() -> SideDrawerController.deletableCell.setOpenedSideDrawer(true));
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
        stockPaneVBox.setAlignment(Pos.TOP_CENTER);
        StackPane.setAlignment(stockPaneVBox, Pos.CENTER);
        stocksInfoPane.getChildren().addAll(stockPaneVBox);
        scrollPaneForStockPane.setContent(stocksInfoPane);
        scrollPaneForStockPane.setHmax(1);
        scrollPaneForStockPane.maxHeightProperty().bind(anchorPane.heightProperty());
        stocksInfoPane.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPaneForStockPane.getViewportBounds().getWidth(), scrollPaneForStockPane.viewportBoundsProperty()));
        stocksInfoPane.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPaneForStockPane.getViewportBounds().getHeight(), scrollPaneForStockPane.viewportBoundsProperty()));
        stocksInfoPane.setMaxHeight(Double.MAX_VALUE);

        mainSplitPane.getItems().add(scrollPaneForStockPane);

        if ("dark".equals(Settings.getTheme())) {
            themeDark.setValue(true);
        } else {
            themeLight.setValue(true);
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
                    JFXDialog wrongInfo = new JFXDialog(stocksInfoPane, content, JFXDialog.DialogTransition.LEFT);
                    JFXButton button = new JFXButton("Okay");
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
                        Platform.runLater(() -> {
                            String selectedStock = SideDrawerController.getSelectedStock();
                            if (linechart == null) {
                                CategoryAxis xAxis = new CategoryAxis();
                                NumberAxis yAxis = new NumberAxis();
                                yAxis.setForceZeroInRange(false);
                                linechart = new LineChart(xAxis, yAxis);
                                linechart.setAnimated(false);
                                linechart.setTitle(selectedStock);
                                linechart.setCreateSymbols(false);
                                linechart.setMaxSize(1200, 450);
                                linechart.setLegendVisible(false);
                                stockPaneVBox.getChildren().addAll(linechart);
                                VBox.setVgrow(linechart, Priority.ALWAYS);
                                linechart.setLegendVisible(false);
                                stocksInfoPane.getChildren().addAll(linechart);
                                stockPaneVBox.getChildren().add(linechart);
//                                sentimentAnalysis(selectedStock);
                                links.setAlignment(Pos.CENTER);

                            } else {
                                linechart.getData().removeAll(Collections.singleton(linechart.getData().setAll()));
                            }
                            linechart.setTitle(selectedStock);
                            String graph = getClass().getResource("/graph-bullish.css").toExternalForm();
                            linechart.getStylesheets().add(graph);
                            loadGraph(selectedStock);
                            if (!stockPaneVBox.getChildren().contains(headers)) {
                                stockPaneVBox.getChildren().add(headers);
                            }

                            if (!stockPaneVBox.getChildren().contains(stats)) {
                                loadData(selectedStock);
                                stats.setSpacing(15);
                                stats.setAlignment(Pos.CENTER);

                                stockPaneVBox.getChildren().add(stats);
                                stockPaneVBox.getChildren().add(links);

                            } else {
//                                sentimentAnalysis(selectedStock);
                                loadData(selectedStock);
                            }
                            loadStockGraph.setValue(false);
                        });
                    }
                }
            }
        });

        logger.info("HomeController has been initialized.", false);
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

    private void clearStyleSheets() {
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

    private void loadGraph(String url) {
        links.getChildren().clear();
        Platform.runLater(() -> {
            linechart.setVisible(true);
            linechart.getData().add(GraphController.getGraphData(StockFetcher.stockDataHistorical(url, 1, "d"), 100));
        });
    }

    private void loadData(String url) {
        JSONObject object = StockFetcher.stockDataCurrent(url);

        if (object != null) {
            open.setText(String.format("%s\n%s", "OPEN", object.get("open")));
            close.setText(String.format("%s\n%s", "CLOSE", object.get("close")));
            volume.setText(String.format("%s\n%s", "VOLUME", object.get("latestVolume")));
            current.setText(String.format("%s\n%s", "CURRENT", object.get("latestPrice")));
            change.setText(String.format("%s\n%s", "CHANGE", object.get("change")));
            low.setText(String.format("%s\n%s", "LOW", object.get("low")));
            high.setText(String.format("%s\n%s", "HIGH", object.get("high")));

            open.setTextAlignment(TextAlignment.CENTER);
            close.setTextAlignment(TextAlignment.CENTER);
            volume.setTextAlignment(TextAlignment.CENTER);
            current.setTextAlignment(TextAlignment.CENTER);
            change.setTextAlignment(TextAlignment.CENTER);
            low.setTextAlignment(TextAlignment.CENTER);
            high.setTextAlignment(TextAlignment.CENTER);

            open.setTextFill(Paint.valueOf("white"));
            close.setTextFill(Paint.valueOf("white"));
            volume.setTextFill(Paint.valueOf("white"));
            current.setTextFill(Paint.valueOf("white"));
            change.setTextFill(Paint.valueOf("white"));
            low.setTextFill(Paint.valueOf("white"));
            high.setTextFill(Paint.valueOf("white"));
        }

        stats.getChildren().addAll(close, volume, current, change, low, high);
    }

    private void sentimentAnalysis(String ticker) {
        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                ArrayList<Article> articles = RSSFeedFetcher.grabArticles(RSSFeedProvider.NASDAQ, RSSFeedProvider.NASDAQ_RSS_FEED, NasdaqArticleRSSFeed.SYMBOL.getValue() + ticker);
                for (int i = 0; i < (articles.size() > 5 ? 5 : articles.size()); i++) {
                    ArticleParser.nasdaqParser(articles.get(i));
                    SentimentAnalyzer.getSentimentScore(articles.get(i));
                }
                Platform.runLater(() -> loadArticleData(articles));
                return null;
            }
        };

        new Thread(task).start();
    }

    private void loadArticleData(ArrayList<Article> articles) {
        for (Article article : articles) {
            System.out.println(article.getSentiment());
            final Hyperlink hyperlink = new Hyperlink(article.getTitle() + " || " + article.getSentiment());
            if(article.getSentiment().contains("bearish")) {
                hyperlink.setTextFill(Paint.valueOf("#c0392b"));
            } else {
                hyperlink.setTextFill(Paint.valueOf("#2ecc71"));
            }
            hyperlink.setOnAction(event -> {
                hs.showDocument(article.getLink());
                event.consume();
            });
            links.getChildren().add(hyperlink);
        }
    }
}
