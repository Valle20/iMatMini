
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TidigareDate extends AnchorPane {

    @FXML private Label dateLabel;
    @FXML private Label priceLabel;


    private Model model = Model.getInstance();

    private Order order;
    private StringBuilder unit = new StringBuilder();

    public TidigareDate(Order order) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TidigareDate.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.order = order;


        dateLabel.setText(getNiceDate());
        priceLabel.setText(getTotalOrderPrice() + " Kr");


    }



    private String getNiceDate(){
        String pattern = "MM/dd/yyyy   HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        String textDate = df.format(order.getDate());

        return textDate;
    }

    private String getTotalOrderPrice(){
        double total = 0;
        List<ShoppingItem> list = order.getItems();
        for(ShoppingItem item: list){
            total += item.getTotal();
        }
        return String.valueOf(total);
    }


}
