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
    }

    @FXML
    private void läggTill(){
        System.out.println("Lägg till " + product.getName());

        model.getShoppingCart().addItem(shoppingItem);

        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);
        plusMinusPane.toFront();

        iMatController.cotainS(shoppingItem);
    }

    @FXML private void plus(){
        shoppingItem.setAmount(shoppingItem.getAmount() + 1);
        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);

        model.getShoppingCart().fireShoppingCartChanged(shoppingItem, true);
        System.out.println("plus" );
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
