package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.List;

public class Card extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML ImageView plusImage;
    @FXML ImageView minusImage;
    @FXML TextField amountTextField;
    @FXML ImageView merInfo;
    @FXML ImageView favourite;
    @FXML AnchorPane plusMinusPane;

    private Model model = Model.getInstance();

    private Product product;

    private StringBuilder unit = new StringBuilder();
    private ShoppingItem shoppingItem;

    public void setShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItem = shoppingItem;
    }

    public ImageView getFavourite() {
        return favourite;
    }

    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

    public TextField getAmountTextField() {
        return amountTextField;
    }

    private iMatController iMatController;

    public Card(ShoppingItem shoppingItem, iMatController iMatController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);



        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.iMatController = iMatController;
        this.shoppingItem = shoppingItem;



        this.product = shoppingItem.getProduct();

        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, 147, 102));

        unit.append(product.getUnit());
        unit.delete(0,3);

/*
        amountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            newValue = newValue.replaceAll("[^0-9]","");
            if (!newValue.equals("")){
                updateAmount(Integer.parseInt(newValue));
            }
        });

 */

    }

    @FXML private void typeAmount(){
        String newValue = amountTextField.getText();
        newValue = newValue.replaceAll("[^0-9]","");
        if (!newValue.equals("")){
            updateAmount(Integer.parseInt(newValue));
        }
    }

    @FXML
    private void läggTill(){
        addItemVarukorg();
    }

    public void addItemVarukorg(){
        System.out.println("Lägg till " + product.getName());

        shoppingItem.setAmount(1);
        model.getShoppingCart().addItem(shoppingItem);

        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);
        plusMinusPane.toFront();
    }

    private void updateAmount(int amount){
        shoppingItem.setAmount(amount);

        if (amount == 0){
            plusMinusPane.toBack();
            model.getShoppingCart().removeItem(shoppingItem);
        }

        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);
        model.getShoppingCart().fireShoppingCartChanged(shoppingItem, true);
    }
    @FXML private void plus(){
        updateAmount((int)(shoppingItem.getAmount() + 1));
    }

    @FXML private void minus(){
        updateAmount((int)(shoppingItem.getAmount() - 1));
    }

    @FXML
    private void merInfo(){
        iMatController.openDetailView();
    }

    @FXML private void gillaVara(){
        if (model.isGillad(product)){
            model.oGillaVara(product);
            favourite.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                    "imatmini/bilder/FavoritesButton.png")));
            System.out.println(" Ogilla vara " + product.getName());
        } else {
            model.gillaVara(product);
            favourite.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                    "imatmini/bilder/filled_heart_button.png")));
            System.out.println(" gilla vara " + product.getName());
        }
    }



}
