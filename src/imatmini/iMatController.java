/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.CartEvent;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCartListener;
import se.chalmers.cse.dat216.project.ShoppingItem;


/**
 *
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {

    // For switching scenes
    SceneController sceneController = new SceneController();

    @FXML
    private void switchToKassa1(ActionEvent event) throws IOException {
        sceneController.switchToKassa1(event);
    }


    // Shopping Pane
   /* @FXML
    private AnchorPane shopPane;
    @FXML
    private TextField searchField;
    @FXML
    private Label itemsLabel;
    @FXML
    private Label costLabel;
    @FXML
    private FlowPane productsFlowPane;
    
    // Account Pane
    @FXML
    private AnchorPane accountPane;
    @FXML 
    ComboBox cardTypeCombo;
    @FXML
    private TextField numberTextField;
    @FXML
    private TextField nameTextField;
    @FXML 
    private ComboBox monthCombo;
    @FXML
    private ComboBox yearCombo;
    @FXML
    private TextField cvcField;
    @FXML
    private Label purchasesLabel;*/

    @FXML private FlowPane varukorgFlowPane;
    @FXML private FlowPane cardsFlowPane;
    @FXML private AnchorPane helpPane;
    @FXML private AnchorPane tidigarePane;
    @FXML private AnchorPane handlaPane;
    // Other variables

    private final Model model = Model.getInstance();



    public void initialize(URL url, ResourceBundle rb) {

        model.initCardMap();

        model.getShoppingCart().addShoppingCartListener(this);

        updateVarukorg(model.getShoppingCart().getItems());
        updateCards(model.getProducts());
        // updateBottomPanel();

        //setupAccountPane();


    }

    private void updateVarukorg(List<ShoppingItem> productList) {
        varukorgFlowPane.getChildren().clear();

        for (ShoppingItem product : productList) {

            varukorgFlowPane.getChildren().add(new VarukorgsItem(product));
        }
    }

    private void updateCards(List<Product> productList) {
        cardsFlowPane.getChildren().clear();
        for (Product product : productList) {

            cardsFlowPane.getChildren().add(model.getCardMap().get(product.getName()));
        }
    }

    @FXML public void gåTillFavoriter(){
        updateCards(model.getGilladeVaror());
    }

    //flytta på Anchorpanes i start

    @FXML
    private void openTidigare(){
        System.out.println("tidigare vyn");
        tidigarePane.toFront();
    }

    @FXML
    private void openHelp(){
        System.out.println("help vyn");
        helpPane.toFront();
    }

    @FXML
    private void openHandla(){
        System.out.println("handla vyn");
        handlaPane.toFront();
    }




    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        updateVarukorg(model.getShoppingCart().getItems());
    }

    @FXML
    private void tömVarukorgen(ActionEvent event) {
        model.clearShoppingCart();
    }

    /*

    // Shop pane actions
    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }
    
    @FXML
    private void handleSearchAction(ActionEvent event) {
        
        List<Product> matches = model.findProducts(searchField.getText());
        updateVarukorg(matches);
        System.out.println("# matching products: " + matches.size());

    }
    
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
    }
    
    @FXML
    private void handleBuyItemsAction(ActionEvent event) {
        model.placeOrder();
        costLabel.setText("Köpet klart!");
    }
    
    // Account pane actions
     @FXML
    private void handleDoneAction(ActionEvent event) {
        closeAccountView();
    }
      


    
    // Navigation
    public void openAccountView() {
        updateAccountPanel();
        accountPane.toFront();
    }

    public void closeAccountView() {
        updateCreditCard();
        shopPane.toFront();
    }
    
    // Shope pane methods
    @Override
     public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
    }

    
    private void updateBottomPanel() {
        
        ShoppingCart shoppingCart = model.getShoppingCart();
        
        itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
        
    }
    
    private void updateAccountPanel() {
        
        CreditCard card = model.getCreditCard();
        
        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
    }
    
    private void updateCreditCard() {
        
        CreditCard card = model.getCreditCard();
        
        card.setCardNumber(numberTextField.getText());
        card.setHoldersName(nameTextField.getText());
        
        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);
        
        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));
        
        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));
        
        card.setVerificationCode(Integer.parseInt(cvcField.getText()));

    }
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());
        
    }*/
}
