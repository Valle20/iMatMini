package imatmini;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import se.chalmers.cse.dat216.project.Customer;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Kassa_1_Controller implements Initializable {

    Model model = Model.getInstance();

    /**
     * på varje sida
     **/
    @FXML
    private Button backToStoreButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateKassaVarukorg(model.getShoppingCart().getItems());

        updateTotalpris();
        updatePersonuppgifter();
        initPersonUppgifterLyssnare();
        initComboboxes();
    }
    /**
     * varukorg sidan
     **/
    @FXML
    private Label momsLabel;
    @FXML
    private Label antalVarorLabel;
    @FXML
    private Label totalPrizeLabel;
    @FXML
    private FlowPane checkoutVarukorgFlowPane;

    public void updateTotalpris(){
        totalPrizeLabel.setText(model.getShoppingCart().getTotal() + " SEK");
        momsLabel.setText(model.getShoppingCart().getTotal() * 0.12 + " SEK");
        antalVarorLabel.setText(getAntalVaror() + " st");
    }

    private int getAntalVaror(){
        int antal = 0;
        for (ShoppingItem s : model.getShoppingCart().getItems()){
            antal += s.getAmount();
        }
        return antal;
    }


    public void updateKassaVarukorg(List<ShoppingItem> productList) {
        checkoutVarukorgFlowPane.getChildren().clear();

        for (ShoppingItem product : productList) {

            checkoutVarukorgFlowPane.getChildren().add(new KassaVarukorgsItem(product, this));
        }
    }

    @FXML private AnchorPane detailPane;
    @FXML
    public void openDetailView() {
        detailPane.toFront();
    }

    @FXML
    public void closeDetailView() {
        detailPane.toBack();
    }
    @FXML
    private AnchorPane varukorgAnchorPane;
    @FXML
    private ImageView nextVarukorgImageView;

    /**
     * personliga uppgifter sidan
     **/

    private Customer customer = model.getCustomer();

    @FXML
    private AnchorPane personligaUppgifterAnchorPane;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField adressTextField;
    @FXML
    private TextField postCodeTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private ImageView nextPersonalImageView;
    @FXML
    private ImageView previousPersonalImageView;

    private void updatePersonuppgifter(){
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        phoneTextField.setText(customer.getPhoneNumber());
        mailTextField.setText(customer.getEmail());
        adressTextField.setText(customer.getAddress());
        postCodeTextField.setText(customer.getPostCode());
        cityTextField.setText(customer.getPostAddress());
        cvckodTextField.setText(model.getCreditCard().getVerificationCode() + "");
        kortnummerTextField.setText(model.getCreditCard().getCardNumber());
        kundnamnTextField.setText(model.getCreditCard().getHoldersName());
    }

    private void initPersonUppgifterLyssnare(){
        kortnummerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.getCreditCard().setCardNumber((newValue));
        });
        kundnamnTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.getCreditCard().setHoldersName((newValue));
        });
        cvckodTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.getCreditCard().setVerificationCode(Integer.parseInt(newValue));
        });
        firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setFirstName(newValue);
        });
        lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setLastName(newValue);
        });
        phoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setPhoneNumber(newValue);
        });
        mailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setEmail(newValue);
        });
        adressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setAddress(newValue);
        });
        postCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setPostCode(newValue);
        });
        cityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setPostAddress(newValue);
        });

    }

    /**
     * Leverans-sidan
     **/
    @FXML
    private AnchorPane leveransAnchorPane;
    @FXML
    private ImageView nextLeveransImageView;
    @FXML
    private ImageView previousLeveransImageView;

    /**
     * Betalning
     **/
    @FXML
    private AnchorPane betalningAnchorPane;
    @FXML
    private Pane betalamedkortPane;
    @FXML
    private AnchorPane slutförtköpAnchorPane;
    @FXML
    private ComboBox korttypComboBox;
    @FXML
    private ComboBox yearComboBox;
    @FXML
    private ComboBox monthComboBox;
    @FXML
    private TextField kundnamnTextField; // ovan sparade
    @FXML
    private TextField kortnummerTextField; // ovan sparade
    @FXML
    private TextField cvckodTextField; // ovan sparade
    @FXML
    private RadioButton kortRadioButton;
    @FXML
    private RadioButton kontantRadioButton;
    @FXML
    private Button slutförköpButton;
    @FXML
    private Button köpklartButton;
    @FXML
    private ImageView previousBetalningImageView;

    private void initComboboxes(){
        monthComboBox.getItems().addAll(model.getMonths());
        yearComboBox.getItems().addAll(model.getYears());
        korttypComboBox.getItems().addAll(model.getCardTypes());

        monthComboBox.getSelectionModel().select(model.getCreditCard().getValidMonth());
        yearComboBox.getSelectionModel().select(model.getCreditCard().getValidYear());
        korttypComboBox.getSelectionModel().select(model.getCreditCard().getCardType());

        monthComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.getCreditCard().setValidMonth(Integer.parseInt(newValue));
            }
        });
        yearComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.getCreditCard().setValidYear(Integer.parseInt(newValue));
            }
        });
        korttypComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.getCreditCard().setCardType((newValue));
            }
        });
    }

    Image nextStepImage = new Image(getClass().getClassLoader().getResourceAsStream("iMatMini/bilder/Nästaknapp.png"));


    // For switching scenes
    SceneController sceneController = new SceneController();


    @FXML
    private void switchAffaren(ActionEvent event) throws IOException {
        sceneController.switchToAffaren(event);
    }

    /**
     * NAVIGERING
     **/
    @FXML
    private void varukorgToFront() {
        System.out.println("Varukorgsvy");
        varukorgAnchorPane.toFront();
    }

    @FXML
    private void personligaUppgifterToFront() {
        System.out.println("personliga uppgifter vyn");
        personligaUppgifterAnchorPane.toFront();
    }

    @FXML
    private void leveransToFront() {
        System.out.println("Leveransvyn");
        leveransAnchorPane.toFront();
    }

    @FXML
    private void betalningToFront() {
        System.out.println("betalningsvy");
        betalningAnchorPane.toFront();
    }
    @FXML
    private void betalamedkortToFront() {
        System.out.println("Betala med kort vy");
        betalamedkortPane.setVisible(true);
    }
    @FXML private void betalamedkortToBack() {
        System.out.println("Betala med kort vy, bort");
        betalamedkortPane.setVisible(false);
    }
    @FXML
    private void slutförtköpToFront() {
        System.out.println("slutfört köp");
        slutförtköpAnchorPane.toFront();
    }


    /** HOVER **/

    @FXML
    private void nextVarukorgMouseEntered() {
        nextVarukorgImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/nästastegHover.png")));
    }

    @FXML
    private void nextVarukorgMouseExited() {
        nextVarukorgImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Nästaknapp.png")));
    }

    @FXML
    private void nextPersonalMouseEntered() {
        nextPersonalImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/nästastegHover.png")));
    }

    @FXML
    private void nextPersonalMouseExited() {
        nextPersonalImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Nästaknapp.png")));
    }

    @FXML
    private void previousPersonalMouseEntered() {
        previousPersonalImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/föregåendeHover.png")));
    }

    @FXML
    private void previousPersonalMouseExited() {
        previousPersonalImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Föregående steg.png")));
    }

    @FXML
    private void nextLeveransMouseEntered() {
        nextLeveransImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/nästastegHover.png")));
    }

    @FXML
    private void nextLeveransMouseExited() {
        nextLeveransImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Nästaknapp.png")));
    }

    @FXML
    private void previousLeveransMouseEntered() {
        previousLeveransImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/föregåendeHover.png")));
    }

    @FXML
    private void previousLeveransMouseExited() {
        previousLeveransImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Föregående steg.png")));
    }
    @FXML
    private void previousBetalningMouseEntered() {
        previousBetalningImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/föregåendeHover.png")));
    }

    @FXML
    private void previousBetalningMouseExited() {
        previousBetalningImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "iMatMini/bilder/Föregående steg.png")));
    }


    /*@FXML
    public void closeButtonMousePressed(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png" )));
    }

   */


    }
