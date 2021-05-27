/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

/**
 *
 * @author oloft
 */
public class VarukorgsItem extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML ImageView plusImage;
    @FXML ImageView minusImage;
    @FXML TextField amountTextField;

    
    private Model model = Model.getInstance();

    private ShoppingItem shoppingItem;
    private StringBuilder unit = new StringBuilder();
    private iMatController parentController;
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public VarukorgsItem(ShoppingItem shoppingItem, iMatController iMatController) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VarukorgsItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        amountTextField.setOnMouseClicked(e -> amountTextField.selectAll());

        unit.append(shoppingItem.getProduct().getUnit());
        unit.delete(0,3);

        this.shoppingItem = shoppingItem;
        this.parentController = iMatController;
        nameLabel.setText(shoppingItem.getProduct().getName());
        prizeLabel.setText(String.format("%.2f", (shoppingItem.getProduct().getPrice()) * shoppingItem.getAmount()) + " kr" );
        imageView.setImage(model.getImage(shoppingItem.getProduct(), 58, 54));
        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);

        iMatController.setTextLimit(amountTextField, 7);
    }

    @FXML private void typeAmount(){
        String newValue = amountTextField.getText();
        newValue = newValue.replaceAll("[^0-9]","");
        if (!newValue.equals("")){
            updateAmount(Integer.parseInt(newValue));
        } else updateAmount((int)shoppingItem.getAmount());
    }

    private void updateAmount(int amount){
        if (amount > 99){
            amount = 99;
        }
        shoppingItem.setAmount(amount);

        if (amount == 0){
            model.getCardMap().get(shoppingItem.getProduct().getName()).plusMinusPane.toBack();
            model.getShoppingCart().removeItem(shoppingItem);
            if (parentController.currentOrder != null){
                parentController.updateOrdersItems(parentController.currentOrder);
            }
        }

        model.getCardMap().get(shoppingItem.getProduct().getName()).getAmountTextField().setText((int)shoppingItem.getAmount() + " " + unit);
        model.getShoppingCart().fireShoppingCartChanged(shoppingItem, true);


    }
    @FXML private void plus(){
        updateAmount((int)(shoppingItem.getAmount() + 1));
    }

    @FXML private void minus(){
        updateAmount((int)(shoppingItem.getAmount() - 1));
    }

}
