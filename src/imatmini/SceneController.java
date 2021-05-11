package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    public SceneController() {
    }


    @FXML
    public void switchToKassa1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Kassa_1_varukorg.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void switchToAffaren(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("iMat.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
