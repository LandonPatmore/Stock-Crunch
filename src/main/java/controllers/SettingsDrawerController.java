package controllers;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsDrawerController implements Initializable  {

    @FXML
    public AnchorPane settingsPane;

    @FXML
    JFXToggleButton lightToggle;

    @FXML
    JFXToggleButton darkToggle;

    @FXML
    public void selectLightTheme(ActionEvent event){
        HomeController.setSetThemLightTrue();
        if (darkToggle.isSelected()){
            darkToggle.setSelected(false);
        }
        if (!lightToggle.isSelected()){
            lightToggle.setSelected(true);
        }
    }

    @FXML
    public void selectLightThemeFromPane(MouseEvent event){
        HomeController.setSetThemLightTrue();
        if (darkToggle.isSelected()){
            darkToggle.setSelected(false);
        }
        if (!lightToggle.isSelected()){
            lightToggle.setSelected(true);
        }
    }

    @FXML
    public void selectDarkTheme(ActionEvent event){
        HomeController.setSetThemDarkTrue();
        if (lightToggle.isSelected()){
            lightToggle.setSelected(false);
        }
        if (!darkToggle.isSelected()){
            darkToggle.setSelected(true);
        }
    }

    @FXML
    public void selectDarkThemeFromPane(MouseEvent event){
        HomeController.setSetThemDarkTrue();
        if (lightToggle.isSelected()){
            lightToggle.setSelected(false);
        }
        if (!darkToggle.isSelected()){
            darkToggle.setSelected(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

}
