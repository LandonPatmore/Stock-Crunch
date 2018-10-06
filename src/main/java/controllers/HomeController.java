package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
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
    JFXButton filtersButton;

    @FXML
    GridPane topBarGridPane;

    @FXML
    JFXDrawer sideDrawer;

    @FXML
    ColumnConstraints gridPaneLeft;

    @FXML
    ColumnConstraints gridPaneCenter;

    @FXML
    ColumnConstraints gridPaneRight;

    private SideDrawerController sideDrawerController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        topBarGridPane.prefWidthProperty().bind(anchorPane.widthProperty());
        mainSplitPane.prefWidthProperty().bind(anchorPane.widthProperty());
        mainSplitPane.prefHeightProperty().bind(anchorPane.heightProperty());
        gridPaneLeft.maxWidthProperty().bind(topBarGridPane.widthProperty());
        gridPaneRight.maxWidthProperty().bind(topBarGridPane.widthProperty());
        sideDrawer.prefHeightProperty().bind(anchorPane.heightProperty());

        sideDrawer.setVisible(false);
        //Allows you to click through the drawer if it's not visible (so we set it invisible when it's not open)
        sideDrawer.setPickOnBounds(false);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Side_Drawer.fxml"));
            VBox box = loader.load();
            sideDrawerController = loader.getController();
            sideDrawerController.dashboardDrawerVBox.maxHeightProperty().bind(anchorPane.heightProperty());
            sideDrawer.setSidePane(box);
        } catch (IOException ex) {
            Logger.getLogger(SideDrawerController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    }
}
