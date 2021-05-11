package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class Kassa_1_Controller {

    // For switching scenes
    SceneController sceneController = new SceneController();

    @FXML
    private void switchAffaren(ActionEvent event) throws IOException {
        sceneController.switchToAffaren(event);
    }

}
