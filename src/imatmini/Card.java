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

import java.io.IOException;

public class Card extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML ImageView plusImage;
    @FXML ImageView minusImage;
    @FXML TextField amountTextField;
    @FXML ImageView merInfo;
    @FXML ImageView favourite;

    private Model model = Model.getInstance();

    private Product product;



    public Card(Product product) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.product = product;
        nameLabel.setText(product.getName());
        prizeLabel.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        imageView.setImage(model.getImage(product, 147, 102));

    }

    @FXML
    private void läggTill(ActionEvent event) {
        System.out.println("Lägg till " + product.getName());
        model.addToShoppingCart(product);
    }

    @FXML private void merInfo(){
        System.out.println(" test mer info knappen ");
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
