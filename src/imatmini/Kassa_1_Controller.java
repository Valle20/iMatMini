package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Kassa_1_Controller {

    @FXML private Button backToStoreButton;
    @FXML private ImageView nextStepImageView;
    @FXML private Label momsLabel;
    @FXML private Label antalVarorLabel;
    @FXML private Label totalPrizeLabel;
    @FXML private FlowPane checkoutVarukorgFlowPane;

    // For switching scenes
    SceneController sceneController = new SceneController();


    @FXML
    private void switchAffaren(ActionEvent event) throws IOException {
        sceneController.switchToAffaren(event);
    }



}
