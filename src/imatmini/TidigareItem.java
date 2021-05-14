package imatmini;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class TidigareItem extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML ImageView plusImage;
    @FXML ImageView minusImage;
    @FXML TextField amountTextField;
    @FXML Label summaLabel;

    private Model model = Model.getInstance();

    private ShoppingItem shoppingItem;
    private StringBuilder unit = new StringBuilder();
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public TidigareItem(ShoppingItem shoppingItem) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TidigareItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        unit.append(shoppingItem.getProduct().getUnit());
        unit.delete(0,3);

        this.shoppingItem = shoppingItem;

        nameLabel.setText(shoppingItem.getProduct().getName());
        prizeLabel.setText( (int)shoppingItem.getAmount() + " x " + shoppingItem.getProduct().getPrice() + " kr");
        summaLabel.setText(String.format("%.2f", (shoppingItem.getProduct().getPrice()) * shoppingItem.getAmount()) + " kr" );
        imageView.setImage(model.getImage(shoppingItem.getProduct(), kImageWidth, kImageWidth*kImageRatio));
    }

    @FXML
    private void läggTill(){
        if (model.getShoppingCart().getItems().contains(shoppingItem)){
            return;
        }
        System.out.println("Lägg till " + shoppingItem.getProduct().getName());

        shoppingItem.setAmount(1);
        model.getShoppingCart().addItem(shoppingItem);

        //amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);
        //plusMinusPane.toFront();
    }

    /*** En del överflödiga metoder, men de får ligga kvar här ***/
    @FXML private void typeAmount(){
        String newValue = amountTextField.getText();
        newValue = newValue.replaceAll("[^0-9]","");
        if (!newValue.equals("")){
            updateAmount(Integer.parseInt(newValue));
        }
    }

    private void updateAmount(int amount){
        shoppingItem.setAmount(amount);

        if (amount == 0){
            model.getCardMap().get(shoppingItem.getProduct().getName()).plusMinusPane.toBack();
            model.getShoppingCart().removeItem(shoppingItem);
        }
        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);
        summaLabel.setText(String.format("%.2f", (shoppingItem.getProduct().getPrice()) * shoppingItem.getAmount()) + " kr" );
        model.getCardMap().get(shoppingItem.getProduct().getName()).getAmountTextField().setText((int)shoppingItem.getAmount() + " " + unit);
        model.getShoppingCart().fireShoppingCartChanged(shoppingItem, true);
    }
    @FXML private void plus(){
        updateAmount((int)(shoppingItem.getAmount() + 1));
        System.out.println("plus");
    }

    @FXML private void minus(){
        updateAmount((int)(shoppingItem.getAmount() - 1));
    }

}
