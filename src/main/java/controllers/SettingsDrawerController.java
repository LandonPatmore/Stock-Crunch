package controllers;

import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import model.Log;
import model.Settings;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsDrawerController implements Initializable  {
    private static final Log logger = new Log(SettingsDrawerController.class);

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
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        if("dark".equals(Settings.getTheme())){
            darkToggle.setSelected(true);
            lightToggle.setSelected(false);
        } else {
            lightToggle.setSelected(true);
            darkToggle.setSelected(false);
        }

        logger.info("SettingsDrawerController has been intialized.", false);
    }

}
