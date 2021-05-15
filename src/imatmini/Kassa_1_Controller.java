package imatmini;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import se.chalmers.cse.dat216.project.Order;
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

        setTextLimit(cvckodTextField, 3);

        chosenDate.setText("");
        if (customer.getMobilePhoneNumber().equals("kort")){
            kortRadioButton.fire();
        }
        if (customer.getMobilePhoneNumber().equals("kontanter")){
            kontantRadioButton.fire();
        }

        //för att få korrekt datum till leveranstidvyn
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));

        String weekday;

        day1.setText("Idag " + dtf.format(now));
        day2.setText("Imorgon " + dtf.format(now.plusDays(1)));
        weekday = translateWeekday(now.plusDays(2).getDayOfWeek().getValue());
        day3.setText(weekday + " " + dtf.format(now.plusDays(2)));
        weekday = translateWeekday(now.plusDays(3).getDayOfWeek().getValue());
        day4.setText(weekday + " " + dtf.format(now.plusDays(3)));
        weekday = translateWeekday(now.plusDays(4).getDayOfWeek().getValue());
        day5.setText(weekday + " " + dtf.format(now.plusDays(4)));
    }

    private void setTextLimit(TextField textField, int length) {
        textField.setOnKeyTyped(event -> {
            String string = textField.getText();

            if (string.length() > length) {
                textField.setText(string.substring(0, length));
                textField.positionCaret(string.length());
            }
        });
    }

    /**
     * Metod för att få dagarna på svenska
     */

    private String translateWeekday(int weekday){
        switch (weekday){
            case 1:
                return "Måndag";
            case 2:
                return "Tisdag";
            case 3:
                return "Onsdag";
            case 4:
                return "Torsdag";
            case 5:
                return "Fredag";
            case 6:
                return "Lördag";
            case 7:
                return "Söndag";
            default:
                return "Måndag";
        }
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

    public void updateTotalpris() {
        antalVarorLabel.setText(getAntalVaror() + " st");

        double pris = model.getShoppingCart().getTotal();
        pris = Math.round(pris * 100.0) / 100.0;
        totalPrizeLabel.setText(pris + " SEK");

        double moms = model.getShoppingCart().getTotal() * 0.12;
        moms = Math.round(moms * 100.0) / 100.0;
        momsLabel.setText(moms + " SEK");
    }

    private int getAntalVaror() {
        int antal = 0;
        for (ShoppingItem s : model.getShoppingCart().getItems()) {
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

    @FXML
    private Label dNamn;
    @FXML
    private ImageView dBild;
    @FXML
    private Label dPris;

    public void populateDetalvy(Product product) {
        dNamn.setText(product.getName());
        dBild.setImage(model.getImage(product, 147, 102));
        dPris.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
    }

    @FXML
    private AnchorPane detailPane;

    @FXML
    public void openDetailView() {
        detailPane.toFront();
    }

    @FXML
    public void closeDetailView() {
        detailPane.toBack();
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
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

    private void updatePersonuppgifter() {
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

    private void initPersonUppgifterLyssnare() {
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

    private boolean radionull = true;


    @FXML
    private Button slutförköpButton;
    @FXML
    private Button köpklartButton;
    @FXML
    private ImageView previousBetalningImageView;

    /** fylla i saker **/



    private void initComboboxes(){
        monthComboBox.getItems().addAll(model.getMonths());
        yearComboBox.getItems().addAll(model.getYears());
        korttypComboBox.getItems().addAll(model.getCardTypes());

        monthComboBox.getSelectionModel().select(""+model.getCreditCard().getValidMonth());
        yearComboBox.getSelectionModel().select(""+model.getCreditCard().getValidYear());
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
     * VAL AV DAG
     */
    @FXML
    private Label chosenDate;
    @FXML
    private Label chosenTime;
    @FXML
    private Label day1;
    @FXML
    private Label day2;
    @FXML
    private Label day3;
    @FXML
    private Label day4;
    @FXML
    private Label day5;

    @FXML
    private void firstDay(){
        chosenDate.setText(day1.getText());
    }
    @FXML
    private void secondDay(){
        chosenDate.setText(day2.getText());
    }
    @FXML
    private void thirdDay(){
        chosenDate.setText(day3.getText());
    }
    @FXML
    private void fourthDay(){
        chosenDate.setText(day4.getText());
    }
    @FXML
    private void fifthDay(){
        chosenDate.setText(day5.getText());
    }

    /**
     * VAL AV TID
     */
    @FXML
    public void firstTime(ActionEvent event) throws IOException {
        chosenTime.setText("8:00 - 12:00");
    }
    @FXML
    public void secondTime(ActionEvent event) throws IOException {
        chosenTime.setText("12:00 - 16:00");
    }
    @FXML
    public void thirdTime(ActionEvent event) throws IOException {
        chosenTime.setText("16:00 - 20:00");
    }

    /**
     * NAVIGERING
     **/
    @FXML
    private void varukorgToFront() {
        System.out.println("Varukorgsvy");
        varukorgAnchorPane.toFront();
    }

    @FXML private Label e1;

    @FXML
    private void personligaUppgifterToFront() {
        if (!model.getShoppingCart().getItems().isEmpty()){
            System.out.println("personliga uppgifter vyn");
            personligaUppgifterAnchorPane.toFront();
            e1.setVisible(false);
        }
        e1.setVisible(true);
    }

    private boolean is2ok(){
        if (
                customer.getAddress().equals("") ||
                        customer.getEmail().equals("") ||
                        customer.getFirstName().equals("") ||
                        customer.getLastName().equals("") ||
                        customer.getPostAddress().equals("") ||
                        customer.getPhoneNumber().equals("") ||
                        customer.getPostCode().equals("")
        ){
            return false;
        } else return true;
    }

    @FXML private Label e2;


    @FXML
    private void leveransToFront() {
        if (is2ok()){
            System.out.println("Leveransvyn");
            leveransAnchorPane.toFront();
            e2.setVisible(false);
        } else {
            e2.setVisible(true);
        }
    }

    @FXML private Label e3;

    @FXML
    private void betalningToFront() {
        if (!chosenDate.getText().equals("")){
            System.out.println("betalningsvy");
            betalningAnchorPane.toFront();
            e3.setVisible(false);
        } else {
            e3.setVisible(true);
        }
    }

    private boolean kortbetalning = false;
    @FXML
    private void betalamedkortToFront() {
        System.out.println("Betala med kort vy");
        betalamedkortPane.setVisible(true);
        kortbetalning = true;
        radionull = false;
        e41.setVisible(false);
        customer.setMobilePhoneNumber("kort"); // används för att valt betalningssätt från tidigare köp ska vara förvalt vid nästa köp
    }
    @FXML private void betalamedkortToBack() {
        System.out.println("Betala med kort vy, bort");
        betalamedkortPane.setVisible(false);
        radionull = false;
        e41.setVisible(false);
        customer.setMobilePhoneNumber("kontanter"); // används för att valt betalningssätt från tidigare köp ska vara förvalt vid nästa köp
    }

    @FXML private Label e41;
    @FXML private Label e42;

    private boolean isKortOk(){
        if (!kortbetalning) return true;
        if (
                model.getCreditCard().getCardNumber().equals("") ||
                model.getCreditCard().getCardType().equals("") ||
                model.getCreditCard().getHoldersName().equals("") ||
                model.getCreditCard().getValidMonth() == 0 ||
                model.getCreditCard().getValidYear() == 0 ||
                model.getCreditCard().getVerificationCode()  == 0
        ){
            return false;
        } else return true;
    }

    @FXML
    private void slutförtköpToFront() {
        if (!radionull && isKortOk()){
            System.out.println("slutfört köp");
            slutförtköpAnchorPane.toFront();
            commitPurchase();
            e42.setVisible(false);
        }
        if (radionull) e41.setVisible(true);
        if (!isKortOk()) e42.setVisible(true);
    }

    private void commitPurchase(){
        model.placeOrder();
        for(int i=0; i < model.getOrders().size(); i++){
            System.out.println( model.getOrders().get(i) );
        }
    }

    /** HOVER **/
/*
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
