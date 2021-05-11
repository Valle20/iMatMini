package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Kassa_1_Controller {

    @FXML private Button backToStoreButton;
    @FXML private ImageView nextStepImageView;
    @FXML private Label momsLabel;
    @FXML private Label antalVarorLabel;
    @FXML private Label totalPrizeLabel;
    // For switching scenes
    SceneController sceneController = new SceneController();

    @FXML
    private void switchAffaren(ActionEvent event) throws IOException {
        sceneController.switchToAffaren(event);
    }

}
