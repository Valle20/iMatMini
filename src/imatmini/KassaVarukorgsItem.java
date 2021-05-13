package imatmini;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import imatmini.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

/**
 *
 * @author oloft
 */
public class KassaVarukorgsItem extends AnchorPane {

    @FXML ImageView imageView;
    @FXML Label nameLabel;
    @FXML Label prizeLabel;
    @FXML ImageView plusImage;
    @FXML ImageView minusImage;
    @FXML TextField amountTextField;
    @FXML Label summaLabel;


    private Model model = Model.getInstance();
    Kassa_1_Controller kassa_1_controller;

    private ShoppingItem shoppingItem;
    private StringBuilder unit = new StringBuilder();
    private final static double kImageWidth = 100.0;
    private final static double kImageRatio = 0.75;

    public KassaVarukorgsItem(ShoppingItem shoppingItem, Kassa_1_Controller kassa_1_controller) {
        this.kassa_1_controller = kassa_1_controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("KassaVarukorgsItem.fxml"));
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
        prizeLabel.setText(String.format("%.2f", shoppingItem.getProduct().getPrice()) + " " + shoppingItem.getProduct().getUnit());
        summaLabel.setText(String.format("%.2f", (shoppingItem.getProduct().getPrice()) * shoppingItem.getAmount()) + " kr" );
        imageView.setImage(model.getImage(shoppingItem.getProduct(), kImageWidth, kImageWidth*kImageRatio));
        amountTextField.setText( (int)shoppingItem.getAmount() + " " + unit);

        /*
        amountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            newValue = newValue.replaceAll("[^0-9]","");
            if (!newValue.equals("")){
                updateAmount(Integer.parseInt(newValue));
            }
        });
         */
    }

    @FXML
    private void merInfo(){
        kassa_1_controller.openDetailView();
    }

    @FXML private void typeAmount(){
        String newValue = amountTextField.getText();
        newValue = newValue.replaceAll("[^0-9]","");
        if (!newValue.equals("")){
            updateAmount(Integer.parseInt(newValue));
        }
    }

    @FXML private void tabort(){
        model.getCardMap().get(shoppingItem.getProduct().getName()).plusMinusPane.toBack();
        model.getShoppingCart().removeItem(shoppingItem);
        shoppingItem.setAmount(0);
        kassa_1_controller.updateKassaVarukorg(model.getShoppingCart().getItems());
        kassa_1_controller.updateTotalpris();
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
        kassa_1_controller.updateTotalpris();
    }
    @FXML private void plus(){
        updateAmount((int)(shoppingItem.getAmount() + 1));
        System.out.println("plus");
    }

    @FXML private void minus(){
        updateAmount((int)(shoppingItem.getAmount() - 1));
    }

}
