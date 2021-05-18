
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingCart;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class OrderItems extends AnchorPane {

    @FXML private Label orderItemName;
    @FXML private Label orderItemAmount;
    @FXML private Label orderItemTotalPrice;
    @FXML private ImageView orderItemImage;
    @FXML private AnchorPane alreadyAddedPane;
    @FXML private AnchorPane notAddedPane;


    private Model model = Model.getInstance();
    private Product product;
    private ShoppingItem shoppingItem;

    public OrderItems(ShoppingItem shoppingItem) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderItems.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        product = shoppingItem.getProduct();
        this.shoppingItem = shoppingItem;

        orderItemName.setText(shoppingItem.getProduct().getName());

        orderItemAmount.setText( (Math.round(shoppingItem.getAmount()) + " x "+shoppingItem.getProduct().getPrice()+ " kr").replaceAll("\\.", "," ));

        orderItemImage.setImage(model.getImage(shoppingItem.getProduct()));

        double pris = shoppingItem.getTotal();
        pris = Math.round(pris * 100.0) / 100.0;
        orderItemTotalPrice.setText(String.format("%.2f",pris) + " kr");

        removeAddButton();
    }

    @FXML
    private void addOrderItemVarukorg(){
        model.getCardMap().get(shoppingItem.getProduct().getName()).addItemVarukorg();
        alreadyAddedPane.toFront();
    }

    private void removeAddButton(){
        ArrayList<Product> productList = new ArrayList<>();
        for (ShoppingItem si : model.getShoppingCart().getItems()){
            productList.add(si.getProduct());
        }
        if (productList.contains(shoppingItem.getProduct())){
            alreadyAddedPane.toFront();
        }
    }


}