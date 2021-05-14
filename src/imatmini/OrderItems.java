
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
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


    private Model model = Model.getInstance();
    private Product product;
    private ShoppingItem shoppingItem;
    private iMatController parentController;

    public OrderItems(ShoppingItem shoppingItem, iMatController iMatController) {

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
        this.parentController = iMatController;

        orderItemName.setText(shoppingItem.getProduct().getName());
        orderItemAmount.setText(Math.round(shoppingItem.getAmount())+" x "+shoppingItem.getProduct().getPrice()+"kr");
        orderItemTotalPrice.setText(String.valueOf(shoppingItem.getTotal()));
        orderItemImage.setImage(model.getImage(shoppingItem.getProduct()));

    }

    @FXML
    private void addOrderItemVarukorg(){

        boolean add = true;
        for (ShoppingItem si : model.getShoppingCart().getItems()){
            System.out.println("finns redan" + si.getProduct().getName());
            System.out.println("läggs till" + shoppingItem.getProduct().getName());
            if (si.getProduct().getName() == shoppingItem.getProduct().getName()){
                add = false;
            }
        }
        System.out.println("t");
        if (add){
            model.getShoppingCart().addItem(shoppingItem);
            parentController.updateVarukorg(model.getShoppingCart().getItems());
        }
      

//        ArrayList<String> productList = new ArrayList<>();
//        for (ShoppingItem si : model.getShoppingCart().getItems()){
//            productList.add(si.getProduct().getName());
//            System.out.println(si.getProduct().getName());
//        }
//        boolean check = Arrays.asList(productList).contains(shoppingItem.getProduct().getName());
//
//        if (!check){
//            System.out.println("t");
//            model.getShoppingCart().addItem(shoppingItem);
//            parentController.updateVarukorg(model.getShoppingCart().getItems());
//        }

//        for (ShoppingItem sp : model.getShoppingCart().getItems()){
//            if (sp.getProduct() == shoppingItem.getProduct()){
//                model.getShoppingCart().addItem(shoppingItem);
//                parentController.updateVarukorg(model.getShoppingCart().getItems());
//            }
//        }
    }


}