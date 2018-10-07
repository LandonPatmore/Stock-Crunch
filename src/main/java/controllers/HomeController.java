package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
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

    public static void setSetThemDarkTrue(){
        setThemeDark.setValue(true);
    }

    public static void setSetThemLightTrue(){
        setThemeLight.setValue(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        //Add css sylesheet to stuff
        addStyleSheets();

        setThemeDark.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    clearStyleSheets();
                    css = this.getClass().getClassLoader().getResource(darkBullish).toExternalForm();
                    addStyleSheets();
                    setThemeLight.setValue(false);
                }
            }
        });

        setThemeLight.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    clearStyleSheets();
                    css = this.getClass().getClassLoader().getResource(lightBearish).toExternalForm();
                    addStyleSheets();
                    setThemeDark.setValue(false);
                }
            }
        });

        //add id to stylesheet
        anchorPane.setId("anchorpane");
        mainSplitPane.setId("split");
        topBarGridPane.setId("topBar");
        sideDrawerController.dashboardDrawerVBox.setId("sidedrawervbox");
        sideDrawerController.listView.setId("sidedrawerlistview");
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
    }

    private void addStyleSheets(){
        anchorPane.getStylesheets().add(css);
        mainSplitPane.getStylesheets().add(css);
        topBarGridPane.getStylesheets().add(css);
        settingsButton.getStylesheets().add(css);
        sideDrawerController.dashboardDrawerVBox.getStylesheets().add(css);
        sideDrawerController.listView.getStylesheets().add(css);
        settingsDrawerController.settingsPane.getStylesheets().add(css);
        settingsDrawerController.lightToggle.getStylesheets().add(css);
        settingsDrawerController.darkToggle.getStylesheets().add(css);
    }

    private void clearStyleSheets(){
        anchorPane.getStylesheets().clear();
        mainSplitPane.getStylesheets().clear();
        topBarGridPane.getStylesheets().clear();
        settingsButton.getStylesheets().clear();
        sideDrawerController.dashboardDrawerVBox.getStylesheets().clear();
        sideDrawerController.listView.getStylesheets().clear();
        settingsDrawerController.settingsPane.getStylesheets().clear();
        settingsDrawerController.lightToggle.getStylesheets().clear();
        settingsDrawerController.darkToggle.getStylesheets().clear();
    }
}
