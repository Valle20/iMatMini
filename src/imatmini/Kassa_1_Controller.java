package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Kassa_1_Controller {
    /** på varje sida **/
    @FXML private Button backToStoreButton;
    @FXML private ImageView nextStepImageView;
    @FXML private ImageView previousStepImageView;

    /** varukorg sidan **/
    @FXML private Label momsLabel;
    @FXML private Label antalVarorLabel;
    @FXML private Label totalPrizeLabel;
    @FXML private FlowPane checkoutVarukorgFlowPane;
    @FXML private AnchorPane varukorgAnchorPane;

    /** personliga uppgifter sidan **/
    @FXML private AnchorPane personligaUppgifterAnchorPane;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField mailTextField;
    @FXML private TextField adressTextField;
    @FXML private TextField postCodeTextField;
    @FXML private TextField cityTextField;

    /** Leverans-sidan **/
    @FXML private AnchorPane leveransAnchorPane;



    Image nextStepImage = new Image(getClass().getClassLoader().getResourceAsStream("iMatMini/bilder/Nästaknapp.png"));



    // For switching scenes
    SceneController sceneController = new SceneController();



    @FXML
    private void switchAffaren(ActionEvent event) throws IOException {
        sceneController.switchToAffaren(event);
    }

    /** NAVIGERING **/
    @FXML
    private void varukorgToFront(){
        System.out.println("Varukorgsvy");
        varukorgAnchorPane.toFront();
    }
    @FXML
    private void personligaUppgifterToFront(){
        System.out.println("personliga uppgifter vyn");
        personligaUppgifterAnchorPane.toFront();
    }
    @FXML
    private void leveransToFront(){
        System.out.println("Leveransvyn");
        leveransAnchorPane.toFront();
    }



    /** HOVER **/

    @FXML
    private void previousStepMouseEntered(){
        previousStepImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/föregåendeHover.png")));
    }
    @FXML
    private void previousStepMouseExited(){
        previousStepImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Föregående steg.png" )));
    }
    @FXML
    private void nextStepMouseEntered(){
        nextStepImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/nästastegHover.png")));
    }
    @FXML
    private void nextStepMouseExited(){
        nextStepImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Nästaknapp.png" )));
    }


    /*@FXML
    public void closeButtonMousePressed(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png" )));
    }

   */



}
