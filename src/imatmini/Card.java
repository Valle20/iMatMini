package imatmini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.util.List;

public class Card extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;

    @FXML TextField amountTextField;
    @FXML ImageView favourite;
    @FXML public AnchorPane plusMinusPane;

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

        amountTextField.setOnMouseClicked(e -> amountTextField.selectAll());

        this.product = shoppingItem.getProduct();

        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, 151  , 102));

        unit.append(product.getUnit());
        unit.delete(0,3);

    }

    @FXML private void typeAmount(){
        String newValue = amountTextField.getText();
        newValue = newValue.replaceAll("[^0-9]","");
        if (!newValue.equals("")){
            updateAmount(Integer.parseInt(newValue));
        } else updateAmount((int)shoppingItem.getAmount());
    }

    @FXML private AnchorPane hjärtknapp;

    @FXML void hHover(){
        hjärtknapp.setStyle("-fx-background-color: #C4C4C4; ");
    }
    @FXML void hHoverN(){
        hjärtknapp.setStyle("-fx-background-color: #E5E5E5; ");
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

        iMatController.e1.setVisible(false);
    }

    public void updateAmount(int amount){
        if (amount > 99){
            amount = 99;
        }
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
        iMatController.populateDetalvy(product);
        iMatController.openDetailView();
    }

    @FXML private void gillaVara(){
        if (model.isGillad(product)){
            model.oGillaVara(product);
            favourite.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                    "imatmini/bilder/outline_favorite_border_black_48dp.png")));
            System.out.println(" Ogilla vara " + product.getName());
        } else {
            model.gillaVara(product);
            favourite.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                    "imatmini/bilder/outline_favorite_black_48dp.png")));
            System.out.println(" gilla vara " + product.getName());
        }
    }



}
