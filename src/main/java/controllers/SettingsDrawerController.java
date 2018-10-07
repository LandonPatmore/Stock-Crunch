package controllers;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Settings;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsDrawerController implements Initializable  {

    @FXML
    public AnchorPane settingsPane;

    @FXML
    public JFXToggleButton lightToggle;

    @FXML
    public JFXToggleButton darkToggle;

    @FXML
    public void selectLightTheme(){
        HomeController.setSetThemLightTrue();
        if (darkToggle.isSelected()){
            darkToggle.setSelected(false);
        }
        if (!lightToggle.isSelected()){
            lightToggle.setSelected(true);
        }

        Settings.setTheme("light");
    }

    @FXML
    public void selectLightThemeFromPane(){
        HomeController.setSetThemLightTrue();
        if (darkToggle.isSelected()){
            darkToggle.setSelected(false);
        }
        if (!lightToggle.isSelected()){
            lightToggle.setSelected(true);
        }

        Settings.setTheme("light");
    }

    @FXML
    public void selectDarkTheme(){
        HomeController.setSetThemDarkTrue();
        if (lightToggle.isSelected()){
            lightToggle.setSelected(false);
        }
        if (!darkToggle.isSelected()){
            darkToggle.setSelected(true);
        }

        Settings.setTheme("dark");
    }

    @FXML
    public void selectDarkThemeFromPane(){
        HomeController.setSetThemDarkTrue();
        if (lightToggle.isSelected()){
            lightToggle.setSelected(false);
        }
        if (!darkToggle.isSelected()){
            darkToggle.setSelected(true);
        }

        Settings.setTheme("dark");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

}
