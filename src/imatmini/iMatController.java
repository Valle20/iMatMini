/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

//import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import se.chalmers.cse.dat216.project.*;


/**
 *
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {

    public void initialize(URL url, ResourceBundle rb) {
        //model.iMatDataHandler.reset();
        initCardMap();

        model.getShoppingCart().addShoppingCartListener(this);

        updateVarukorg(model.getShoppingCart().getItems());
        updateTotalpris();
        updateOrders();
        startsida();

        sökruta.setOnMouseClicked(e -> sökruta.selectAll());

        // updateBottomPanel();
        //setupAccountPane();

        helpViewAddPicture.setImage(new Image("imatmini/bilder/addButton.png"));
        helpViewInfoPicture.setImage(new Image("imatmini/bilder/moreInfo.png"));
        helpViewCheckout.setImage(new Image("imatmini/bilder/Checkout.png"));
        helpViewHeart.setImage(new Image("imatmini/bilder/empty_heart.png"));

        updatePersonuppgifter();
        initPersonUppgifterLyssnare();
        initComboboxes();

        setTextLimit(cvckodTextField, 3);

        chosenDate.setText("");
        if (customer.getMobilePhoneNumber().equals("kort")){
            kortRadioButton.fire();
        }
        if (customer.getMobilePhoneNumber().equals("kontanter")){
            kontantRadioButton.fire();
        }

        //för att få korrekt datum till leveranstidvyn
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));

        String weekday;

        day1.setText("Idag " + dtf.format(now));
        day2.setText("Imorgon " + dtf.format(now.plusDays(1)));
        weekday = translateWeekday(now.plusDays(2).getDayOfWeek().getValue());
        day3.setText(weekday + " " + dtf.format(now.plusDays(2)));
        weekday = translateWeekday(now.plusDays(3).getDayOfWeek().getValue());
        day4.setText(weekday + " " + dtf.format(now.plusDays(3)));
        weekday = translateWeekday(now.plusDays(4).getDayOfWeek().getValue());
        day5.setText(weekday + " " + dtf.format(now.plusDays(4)));
    }

    Order currentOrder;

    @FXML
    private void switchToKassa() {
        if (model.getShoppingCart().getItems().isEmpty()){
            e1.setVisible(true);
        } else {
            varukorgAnchorPane.toFront();
            System.out.println("switch to Kassa");
            updateKassaVarukorg(model.getShoppingCart().getItems());
            updateTotalprisKassa();
            updateTotalpris();
            e11.setVisible(false);
        }
    }

    @FXML private FlowPane varukorgFlowPane;
    @FXML private FlowPane cardsFlowPane;
    @FXML private AnchorPane helpPane;
    @FXML private AnchorPane tidigarePane;
    @FXML private AnchorPane handlaPane;
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane detailPane;
    @FXML private AnchorPane kategorierPane;
    @FXML private FlowPane dateFlowPane;
    //Bilder för helpvyn
    @FXML private ImageView helpViewAddPicture;
    @FXML private ImageView helpViewInfoPicture;
    @FXML private ImageView helpViewCheckout;
    @FXML private ImageView helpViewHeart;

    @FXML private FlowPane orderItemFlowPane;
    // Other variables

    private final Model model = Model.getInstance();

    public void initCardMap(){
        for (Product product : Model.getInstance().getProducts()){
            Card card = new Card(new ShoppingItem(product), this);
            model.getCardMap().put(product.getName(), card);
            initFavourites(product);
        }

        List<ShoppingItem> varukorgItems =  model.getShoppingCart().getItems();
        for (ShoppingItem item : varukorgItems){
            model.getCardMap().get(item.getProduct().getName()).plusMinusPane.toFront();
            model.getCardMap().get(item.getProduct().getName()).setShoppingItem(item);

            StringBuilder unit = new StringBuilder();
            unit.append(item.getProduct().getUnit());
            unit.delete(0,3);
            model.getCardMap().get(item.getProduct().getName()).amountTextField.setText( (int)item.getAmount() + " " + unit);
        }
    }


    public void initFavourites(Product product){
        if (model.isGillad(product)){
            model.getCardMap().get(product.getName()).favourite.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                    "imatmini/bilder/outline_favorite_black_48dp.png")));
        }
    }


    public void updateVarukorg(List<ShoppingItem> productList) {
        varukorgFlowPane.getChildren().clear();
        ArrayList<ShoppingItem> list = new ArrayList<>();
        for (ShoppingItem item : productList){
            list.add(item);
        }
        Collections.reverse(list);
        for (ShoppingItem product : list) {

            varukorgFlowPane.getChildren().add(new VarukorgsItem(product, this));

        }
    }


    public void updateOrdersItems(Order order) {
        currentOrder = order;
        orderItemFlowPane.getChildren().clear();

        for (ShoppingItem shoppingItem : order.getItems()) {
            orderItemFlowPane.getChildren().add(new OrderItems(shoppingItem));
        }
    }

    public void updateOrders() {
        dateFlowPane.getChildren().clear();
        ArrayList<Order> orderList = new ArrayList<>();
        for (Order order : model.getOrders()){
            orderList.add(order);
        }
        Collections.reverse(orderList);
        for (Order sortedOrder : orderList) {
            dateFlowPane.getChildren().add(new TidigareDate(sortedOrder,this));
        }

    }

//    @FXML
//    public void addOrder(){
//      model.placeOrder();
//        Order order = new Order();
//        order.setDate(new java.sql.Date(System.currentTimeMillis()));
//        System.out.println(order.getDate());
//        updateOrders(model.getOrders());
//    }

    @FXML private AnchorPane titelPane;
    @FXML private Label titelLabel;

    public void updateCards(List<Product> productList) {
        kategorierPane.toBack();
        cardsFlowPane.getChildren().clear();
        cardsFlowPane.getChildren().add(titelPane);
        int i = 0;
        for (Product product : productList) {
            i++;

            cardsFlowPane.getChildren().add(model.getCardMap().get(product.getName()));
        }
    }

    @FXML private void läggTillAllaVaror(){
        updateOrdersItems(currentOrder);
        ArrayList<Product> productListVarukorgen = new ArrayList<>();
        for (ShoppingItem sIV : model.getShoppingCart().getItems()){
            productListVarukorgen.add(sIV.getProduct());
        }
        for (ShoppingItem sIO : currentOrder.getItems()){
            if (productListVarukorgen.contains(sIO.getProduct())){
                ;
            }else{
                model.getCardMap().get(sIO.getProduct().getName()).addItemVarukorg();
            }

        }
        updateOrdersItems(currentOrder);
    }

    @FXML public void allaVaror(){
        titelLabel.setText("Alla varor");
        updateCards(model.getProducts());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    public List<Product> getMejeri(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            if (product.getCategory() == ProductCategory.DAIRIES){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void mejeri(){
        titelLabel.setText("Mejeri");
        updateCards(getMejeri());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    public List<Product> getSötsaker(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            if (product.getCategory() == ProductCategory.SWEET){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void sötsaker(){
        titelLabel.setText("Sötsaker");
        updateCards(getSötsaker());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    public List<Product> getKött(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            ProductCategory p = product.getCategory();
            if ((
                    p == ProductCategory.FISH
                    || p == ProductCategory.MEAT
            )){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void Kött(){
        titelLabel.setText("Kött & Fisk");
        updateCards(getKött());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }
    public List<Product> getDryck(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            ProductCategory p = product.getCategory();
            if ((
                    p == ProductCategory.HOT_DRINKS
                    || p == ProductCategory.COLD_DRINKS
            )){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void Dryck(){
        titelLabel.setText("Dryck");
        updateCards(getDryck());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    public List<Product> getBröd(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            if (product.getCategory() == ProductCategory.BREAD){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void bröd(){
        titelLabel.setText("Bröd");
        updateCards(getBröd());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    public List<Product> getSkafferi(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            ProductCategory p = product.getCategory();
            if ((
                    p == ProductCategory.FLOUR_SUGAR_SALT
                    || p == ProductCategory.PASTA
                    || p == ProductCategory.POTATO_RICE
                    || p == ProductCategory.HERB
                    || p == ProductCategory.NUTS_AND_SEEDS
            )){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void Skafferi(){
        titelLabel.setText("Skafferi");
        updateCards(getSkafferi());
        handlaPane.toFront();
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    public List<Product> getFruktGrönt(){
        List<Product> list = new ArrayList<>();
        for (Product product : model.getProducts()){
            ProductCategory p = product.getCategory();
            if ((
                    p == ProductCategory.CITRUS_FRUIT
                    || p == ProductCategory.EXOTIC_FRUIT
                    || p == ProductCategory.MELONS
                    || p == ProductCategory.CABBAGE
                    || p == ProductCategory.BERRY
                    || p == ProductCategory.FRUIT
                    || p == ProductCategory.ROOT_VEGETABLE
                    || p == ProductCategory.VEGETABLE_FRUIT
            )){
                list.add(product);
            }
        }
        return list;
    }

    @FXML public void FruktGrönt(){
        titelLabel.setText("Frukt & Grönt");
        updateCards(getFruktGrönt());
        handlaPane.toFront();

        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    @FXML private Button startsida;
    @FXML private Button favpriter;
    @FXML private Button tidigare;
    @FXML private Button hjälp;


    @FXML public void gåTillFavoriter(){
        titelLabel.setText("Favoriter");
        updateCards(model.getGilladeVaror());
        handlaPane.toFront();
        favpriter.getStyleClass().add("toolbar-btn-tryckt");
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn"));
        tidigare.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");
        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    //flytta på Anchorpanes i start

    @FXML
    private void openKategoriere(){
        System.out.println("kategorier");
        kategorierPane.toFront();
    }

    @FXML
    private void stangKategoriere(){
        kategorierPane.toBack();
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }
    @FXML
    private void openTidigare(){
        updateOrders();
        System.out.println("tidigare vyn");
        tidigarePane.toFront();
        tidigare.getStyleClass().add("toolbar-btn-tryckt");
        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn"));
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    @FXML
    private void openHelp(){
        System.out.println("help vyn");
        helpPane.toFront();
        hjälp.getStyleClass().add("toolbar-btn-tryckt");
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn"));


        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");
        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    @FXML
    private void openHandla(){
        System.out.println("handla vyn");
        handlaPane.toFront();

        updateCards(model.getProducts()); // lägger alla kort i här så länge
        titelLabel.setText("Startsida eller veckans varor");
    }

    @FXML private AnchorPane titelPane1;
    @FXML private Label titelLabel1;

    public void startsida() {
        mainPane.toFront();
        handlaPane.toFront();
        kategorierPane.toBack();
        cardsFlowPane.getChildren().clear();
        titelLabel.setText("Veckans varor");
        titelLabel1.setText("Säsongsvaror");

        cardsFlowPane.getChildren().add(titelPane);
        cardsFlowPane.getChildren().add(model.getCardMap().get("Citron"));
        cardsFlowPane.getChildren().add(model.getCardMap().get("Choklad"));
        cardsFlowPane.getChildren().add(model.getCardMap().get("Broccoli"));
        cardsFlowPane.getChildren().add(model.getCardMap().get("Mozzarella"));

        cardsFlowPane.getChildren().add(titelPane1);
        cardsFlowPane.getChildren().add(model.getCardMap().get("Fruktsoppa"));
        cardsFlowPane.getChildren().add(model.getCardMap().get("Brie"));
        cardsFlowPane.getChildren().add(model.getCardMap().get("Vattenmelon"));

        startsida.getStyleClass().add("toolbar-btn-tryckt");
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn"));

        tidigare.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));


    }

    @Override
    public void shoppingCartChanged(CartEvent cartEvent) {
        updateVarukorg(model.getShoppingCart().getItems());
        updateTotalpris();
        updateTotalprisKassa();
    }

    @FXML
    private void tömVarukorgen(ActionEvent event) {
        List<ShoppingItem> savekart = new ArrayList<>();
        savekart.addAll(model.getShoppingCart().getItems());
        model.clearShoppingCart();
        for (ShoppingItem item : savekart){
            model.getCardMap().get(item.getProduct().getName()).updateAmount(0);
        }
        if (currentOrder != null){
            updateOrdersItems(currentOrder);
        }
    }


    @FXML
    public void openDetailView() {
        detailPane.toFront();
    }

    @FXML
    public void closeDetailView() {
        mainPane.toFront();
    }

    @FXML private TextField sökruta;

    @FXML
    private void sök(ActionEvent event) {
        openHandla();
        titelLabel.setText("Sökord: \"" + sökruta.getText() + "\"");
        List<Product> matches = model.findProducts(sökruta.getText());
        if (matches.isEmpty()){
            titelLabel.setText("Din sökning: \"" + sökruta.getText() + "\" gav inga träffar");
        }
        updateCards(matches);
        System.out.println("# matching products: " + matches.size());
        tidigare.getStyleClass().add("toolbar-btn");
        favpriter.getStyleClass().add("toolbar-btn");
        hjälp.getStyleClass().add("toolbar-btn");
        startsida.getStyleClass().add("toolbar-btn");

        tidigare.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        favpriter.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        startsida.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
        hjälp.getStyleClass().removeIf(style -> style.equals("toolbar-btn-tryckt"));
    }

    @FXML Label totaltPris;
    @FXML
    public void updateTotalpris() {
        double pris = model.getShoppingCart().getTotal();
        pris = Math.round(pris * 100.0) / 100.0;
        totaltPris.setText(String.format("%.2f",pris) + " SEK");
    }

    /****************************************** KASSA *********************************************/

    /**
     * på varje sida
     **/
    @FXML
    private Button backToStoreButton;

    /*
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateKassaVarukorg(model.getShoppingCart().getItems());

        updateTotalprisKassa();
        updatePersonuppgifter();
        initPersonUppgifterLyssnare();
        initComboboxes();

        setTextLimit(cvckodTextField, 3);

        chosenDate.setText("");
        if (customer.getMobilePhoneNumber().equals("kort")){
            kortRadioButton.fire();
        }
        if (customer.getMobilePhoneNumber().equals("kontanter")){
            kontantRadioButton.fire();
        }

        //för att få korrekt datum till leveranstidvyn
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));

        String weekday;

        day1.setText("Idag " + dtf.format(now));
        day2.setText("Imorgon " + dtf.format(now.plusDays(1)));
        weekday = translateWeekday(now.plusDays(2).getDayOfWeek().getValue());
        day3.setText(weekday + " " + dtf.format(now.plusDays(2)));
        weekday = translateWeekday(now.plusDays(3).getDayOfWeek().getValue());
        day4.setText(weekday + " " + dtf.format(now.plusDays(3)));
        weekday = translateWeekday(now.plusDays(4).getDayOfWeek().getValue());
        day5.setText(weekday + " " + dtf.format(now.plusDays(4)));
    }

     */

    private void setTextLimit(TextField textField, int length) {
        textField.setOnKeyTyped(event -> {
            String string = textField.getText();

            if (string.length() > length) {
                textField.setText(string.substring(0, length));
                textField.positionCaret(string.length());
            }
        });
    }

    /**
     * Metod för att få dagarna på svenska
     */

    private String translateWeekday(int weekday){
        switch (weekday){
            case 1:
                return "Måndag";
            case 2:
                return "Tisdag";
            case 3:
                return "Onsdag";
            case 4:
                return "Torsdag";
            case 5:
                return "Fredag";
            case 6:
                return "Lördag";
            case 7:
                return "Söndag";
            default:
                return "Måndag";
        }
    }


    /**
     * varukorg sidan
     **/
    @FXML
    private Label momsLabel;
    @FXML
    private Label antalVarorLabel;
    @FXML
    private Label totalPrizeLabel;
    @FXML
    private FlowPane checkoutVarukorgFlowPane;

    public void updateTotalprisKassa() {
        antalVarorLabel.setText(getAntalVaror() + " st");

        double pris = model.getShoppingCart().getTotal();
        pris = Math.round(pris * 100.0) / 100.0;
        totalPrizeLabel.setText(String.format("%.2f",pris) + " SEK");

        double moms = model.getShoppingCart().getTotal() * 0.12;
        moms = Math.round(moms * 100.0) / 100.0;
        momsLabel.setText(String.format("%.2f",moms) + " SEK");
    }

    private int getAntalVaror() {
        int antal = 0;
        for (ShoppingItem s : model.getShoppingCart().getItems()) {
            antal += s.getAmount();
        }
        return antal;
    }


    public void updateKassaVarukorg(List<ShoppingItem> productList) {
        checkoutVarukorgFlowPane.getChildren().clear();

        for (ShoppingItem product : productList) {

            checkoutVarukorgFlowPane.getChildren().add(new KassaVarukorgsItem(product, this));
        }
    }

    @FXML
    private Label dNamn;
    @FXML
    private ImageView dBild;

    @FXML private Text eko;

    public void populateDetalvy(Product product) {
        dNamn.setText(product.getName());
        dBild.setImage(model.getImage(product, 147, 102));
        eko.setVisible(product.isEcological());
    }


    @FXML
    private AnchorPane varukorgAnchorPane;
    @FXML
    private ImageView nextVarukorgImageView;

    /**
     * personliga uppgifter sidan
     **/

    private Customer customer = model.getCustomer();

    @FXML
    private AnchorPane personligaUppgifterAnchorPane;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField adressTextField;
    @FXML
    private TextField postCodeTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private ImageView nextPersonalImageView;
    @FXML
    private ImageView previousPersonalImageView;

    private void updatePersonuppgifter() {
        firstNameTextField.setText(customer.getFirstName());
        lastNameTextField.setText(customer.getLastName());
        phoneTextField.setText(customer.getPhoneNumber());
        mailTextField.setText(customer.getEmail());
        adressTextField.setText(customer.getAddress());
        postCodeTextField.setText(customer.getPostCode());
        cityTextField.setText(customer.getPostAddress());
        cvckodTextField.setText(model.getCreditCard().getVerificationCode() + "");
        if (model.getCreditCard().getVerificationCode() == 0){
            cvckodTextField.setText("");
        }
        kortnummerTextField.setText(model.getCreditCard().getCardNumber());
        kundnamnTextField.setText(model.getCreditCard().getHoldersName());
    }

    private void initPersonUppgifterLyssnare() {
        kortnummerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.getCreditCard().setCardNumber((newValue));
        });
        kundnamnTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.getCreditCard().setHoldersName((newValue));
        });
        cvckodTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            model.getCreditCard().setVerificationCode(Integer.parseInt(newValue));
        });
        firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setFirstName(newValue);
        });
        lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setLastName(newValue);
        });
        phoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setPhoneNumber(newValue);
        });
        mailTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setEmail(newValue);
        });
        adressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setAddress(newValue);
        });
        postCodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setPostCode(newValue);
        });
        cityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            customer.setPostAddress(newValue);
        });

    }

    /**
     * Leverans-sidan
     **/
    @FXML
    private AnchorPane leveransAnchorPane;
    @FXML
    private ImageView nextLeveransImageView;
    @FXML
    private ImageView previousLeveransImageView;

    /**
     * Betalning
     **/
    @FXML
    private AnchorPane betalningAnchorPane;
    @FXML
    private Pane betalamedkortPane;
    @FXML
    private AnchorPane slutförtköpAnchorPane;
    @FXML
    private ComboBox korttypComboBox;
    @FXML
    private ComboBox yearComboBox;
    @FXML
    private ComboBox monthComboBox;
    @FXML
    private TextField kundnamnTextField; // ovan sparade
    @FXML
    private TextField kortnummerTextField; // ovan sparade
    @FXML
    private TextField cvckodTextField; // ovan sparade
    @FXML
    private RadioButton kortRadioButton;
    @FXML
    private RadioButton kontantRadioButton;

    private boolean radionull = true;


    @FXML
    private Button slutförköpButton;
    @FXML
    private Button köpklartButton;
    @FXML
    private ImageView previousBetalningImageView;

    /** fylla i saker **/



    private void initComboboxes(){
        monthComboBox.setVisibleRowCount(12);

        monthComboBox.getItems().addAll(model.getMonths());
        yearComboBox.getItems().addAll(model.getYears());
        korttypComboBox.getItems().addAll(model.getCardTypes());

        monthComboBox.getSelectionModel().select(model.getCreditCard().getValidMonth() + "");
        yearComboBox.getSelectionModel().select(model.getCreditCard().getValidYear() + "");
        korttypComboBox.getSelectionModel().select(model.getCreditCard().getCardType());

        monthComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.getCreditCard().setValidMonth(Integer.parseInt(newValue));
            }
        });
        yearComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.getCreditCard().setValidYear(Integer.parseInt(newValue));
            }
        });
        korttypComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                model.getCreditCard().setCardType((newValue));
            }
        });
    }

    Image nextStepImage = new Image(getClass().getClassLoader().getResourceAsStream("iMatMini/bilder/Nästaknapp.png"));


    @FXML
    private void switchAffaren(ActionEvent event) throws IOException {
        mainPane.toFront();
        startsida();
    }

    /**
     * VAL AV DAG
     */

    @FXML private Button a;
    @FXML private Button b;
    @FXML private Button c;
    @FXML private Button d;
    @FXML private Button e;
    @FXML private Button f;
    @FXML private Button gg;
    @FXML private Button h;
    @FXML private Button i;
    @FXML private Button j;
    @FXML private Button k;
    @FXML private Button l;
    @FXML private Button m;
    @FXML private Button n;
    @FXML private Button o;

    private void updateColorTrycktButton(Button button){
        a.getStyleClass().add("btn-checkout");
        a.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        b.getStyleClass().add("btn-checkout");
        b.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        c.getStyleClass().add("btn-checkout");
        c.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        d.getStyleClass().add("btn-checkout");
        d.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        e.getStyleClass().add("btn-checkout");
        e.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        f.getStyleClass().add("btn-checkout");
        f.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        gg.getStyleClass().add("btn-checkout");
        gg.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        h.getStyleClass().add("btn-checkout");
        h.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        i.getStyleClass().add("btn-checkout");
        i.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        j.getStyleClass().add("btn-checkout");
        j.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        k.getStyleClass().add("btn-checkout");
        k.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        l.getStyleClass().add("btn-checkout");
        l.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        m.getStyleClass().add("btn-checkout");
        m.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        n.getStyleClass().add("btn-checkout");
        n.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        o.getStyleClass().add("btn-checkout");
        o.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));

        button.getStyleClass().add("btn-checkout-tryckt");
        button.getStyleClass().removeIf(style -> style.equals("btn-checkout"));
    }

    private void clearColorTrycktButton(){
        a.getStyleClass().add("btn-checkout");
        a.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        b.getStyleClass().add("btn-checkout");
        b.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        c.getStyleClass().add("btn-checkout");
        c.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        d.getStyleClass().add("btn-checkout");
        d.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        e.getStyleClass().add("btn-checkout");
        e.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        f.getStyleClass().add("btn-checkout");
        f.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        gg.getStyleClass().add("btn-checkout");
        gg.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        h.getStyleClass().add("btn-checkout");
        h.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        i.getStyleClass().add("btn-checkout");
        i.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        j.getStyleClass().add("btn-checkout");
        j.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        k.getStyleClass().add("btn-checkout");
        k.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        l.getStyleClass().add("btn-checkout");
        l.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        m.getStyleClass().add("btn-checkout");
        m.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        n.getStyleClass().add("btn-checkout");
        n.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
        o.getStyleClass().add("btn-checkout");
        o.getStyleClass().removeIf(style -> style.equals("btn-checkout-tryckt"));
    }

    @FXML private void a(){
        firstDay();
        firstTime();
        updateColorTrycktButton(a);
    }
    @FXML private void b(){
        firstDay();
        secondTime();
        updateColorTrycktButton(b);
    }
    @FXML private void c(){
        firstDay();
        thirdTime();
        updateColorTrycktButton(c);
    }
    @FXML private void d(){
        secondDay();
        firstTime();
        updateColorTrycktButton(d);
    }
    @FXML private void e(){
        secondDay();
        secondTime();
        updateColorTrycktButton(e);
    }
    @FXML private void f(){
        secondDay();
        thirdTime();
        updateColorTrycktButton(f);
    }
    @FXML private void gg(){
        thirdDay();
        firstTime();
        updateColorTrycktButton(gg);
    }
    @FXML private void h(){
        thirdDay();
        secondTime();
        updateColorTrycktButton(h);
    }
    @FXML private void i(){
        thirdDay();
        thirdTime();
        updateColorTrycktButton(i);
    }
    @FXML private void j(){
        fourthDay();
        firstTime();
        updateColorTrycktButton(j);
    }
    @FXML private void k(){
        fourthDay();
        secondTime();
        updateColorTrycktButton(k);
    }
    @FXML private void l(){
        fourthDay();
        thirdTime();
        updateColorTrycktButton(l);
    }
    @FXML private void m(){
        fifthDay();
        firstTime();
        updateColorTrycktButton(m);
    }
    @FXML private void n(){
        fifthDay();
        secondTime();
        updateColorTrycktButton(n);
    }
    @FXML private void o(){
        fifthDay();
        thirdTime();
        updateColorTrycktButton(o);
    }


    @FXML private AnchorPane duharvaltPane;

    @FXML
    private Label chosenDate;
    @FXML
    private Label chosenTime;
    @FXML
    private Label day1;
    @FXML
    private Label day2;
    @FXML
    private Label day3;
    @FXML
    private Label day4;
    @FXML
    private Label day5;

    @FXML
    private void firstDay(){
        chosenDate.setText(day1.getText());
    }
    @FXML
    private void secondDay(){
        chosenDate.setText(day2.getText());
    }
    @FXML
    private void thirdDay(){
        chosenDate.setText(day3.getText());
    }
    @FXML
    private void fourthDay(){
        chosenDate.setText(day4.getText());
    }
    @FXML
    private void fifthDay(){
        chosenDate.setText(day5.getText());
    }

    /**
     * VAL AV TID
     */
    @FXML
    public void firstTime(){
        chosenTime.setText("8:00 - 12:00");
        duharvaltPane.setVisible(true);
    }
    @FXML
    public void secondTime(){
        chosenTime.setText("12:00 - 16:00");
        duharvaltPane.setVisible(true);
    }
    @FXML
    public void thirdTime(){
        chosenTime.setText("16:00 - 20:00");
        duharvaltPane.setVisible(true);
    }

    /**
     * NAVIGERING
     **/
    @FXML
    private void varukorgToFront() {
        System.out.println("Varukorgsvy");
        varukorgAnchorPane.toFront();
    }

    @FXML public Label e1;
    @FXML public Label e11;

    @FXML
    private void personligaUppgifterToFront() {
        if (!model.getShoppingCart().getItems().isEmpty()){
            System.out.println("personliga uppgifter vyn");
            personligaUppgifterAnchorPane.toFront();
            e11.setVisible(false);
        } else {
            e11.setVisible(true);
        }
    }

    private boolean is2ok(){
        if (
                customer.getAddress().equals("") ||
                        customer.getEmail().equals("") ||
                        customer.getFirstName().equals("") ||
                        customer.getLastName().equals("") ||
                        customer.getPostAddress().equals("") ||
                        customer.getPhoneNumber().equals("") ||
                        customer.getPostCode().equals("")
        ){
            return false;
        } else return true;
    }

    @FXML private Label e2;


    @FXML
    private void leveransToFront() {
        if (is2ok()){
            System.out.println("Leveransvyn");
            leveransAnchorPane.toFront();
            e2.setVisible(false);
        } else {
            e2.setVisible(true);
        }
    }

    @FXML private Label e3;

    @FXML
    private void betalningToFront() {
        if (!chosenDate.getText().equals("")){
            System.out.println("betalningsvy");
            betalningAnchorPane.toFront();
            e3.setVisible(false);
        } else {
            e3.setVisible(true);
        }
    }

    private boolean kortbetalning = false;
    @FXML
    private void betalamedkortToFront() {
        System.out.println("Betala med kort vy");
        betalamedkortPane.setVisible(true);
        kortbetalning = true;
        radionull = false;
        e41.setVisible(false);
        customer.setMobilePhoneNumber("kort"); // används för att valt betalningssätt från tidigare köp ska vara förvalt vid nästa köp
    }
    @FXML private void betalamedkortToBack() {
        System.out.println("Betala med kort vy, bort");
        betalamedkortPane.setVisible(false);
        kortbetalning = false;
        radionull = false;
        e41.setVisible(false);
        customer.setMobilePhoneNumber("kontanter"); // används för att valt betalningssätt från tidigare köp ska vara förvalt vid nästa köp
    }

    @FXML private Label e41;
    @FXML private Label e42;

    private boolean isKortOk(){
        if (!kortbetalning) return true;
        if (
                model.getCreditCard().getCardNumber().equals("") ||
                        model.getCreditCard().getCardType().equals("") ||
                        model.getCreditCard().getHoldersName().equals("") ||
                        model.getCreditCard().getValidMonth() == 0 ||
                        model.getCreditCard().getValidYear() == 0 ||
                        model.getCreditCard().getVerificationCode()  == 0
        ){
            return false;
        } else return true;
    }

    @FXML Label bekräftelse;

    @FXML
    private void slutförtköpToFront() {
        if (!radionull && isKortOk()){
            System.out.println("slutfört köp");
            bekräftelse.setText("Ett bekräftelsemail är skickat till din Email: " + customer.getEmail());
            slutförtköpAnchorPane.toFront();
            commitPurchase();
            e42.setVisible(false);
        }
        if (radionull) e41.setVisible(true);
        if (!isKortOk()) e42.setVisible(true);
    }

    private void commitPurchase(){
        List<ShoppingItem> savekart = new ArrayList<>();
        savekart.addAll(model.getShoppingCart().getItems());

        model.placeOrder();

        for (ShoppingItem item : savekart){
            model.getCardMap().get(item.getProduct().getName()).plusMinusPane.toBack();
        }

        clearColorTrycktButton();

        for(int i=0; i < model.getOrders().size(); i++){
            System.out.println( model.getOrders().get(i) );
        }
    }


    /*

    // Shop pane actions
    @FXML
    private void handleShowAccountAction(ActionEvent event) {
        openAccountView();
    }
    
    @FXML
    private void handleSearchAction(ActionEvent event) {
        
        List<Product> matches = model.findProducts(searchField.getText());
        updateVarukorg(matches);
        System.out.println("# matching products: " + matches.size());
    }
    
    @FXML
    private void handleClearCartAction(ActionEvent event) {
        model.clearShoppingCart();
    }
    
    @FXML
    private void handleBuyItemsAction(ActionEvent event) {
        model.placeOrder();
        costLabel.setText("Köpet klart!");
    }
    
    // Account pane actions
     @FXML
    private void handleDoneAction(ActionEvent event) {
        closeAccountView();
    }

    // Navigation
    public void openAccountView() {
        updateAccountPanel();
        accountPane.toFront();
    }

    public void closeAccountView() {
        updateCreditCard();
        shopPane.toFront();
    }
    
    // Shope pane methods
    @Override
     public void shoppingCartChanged(CartEvent evt) {
        updateBottomPanel();
    }

    
    private void updateBottomPanel() {
        
        ShoppingCart shoppingCart = model.getShoppingCart();
        
        itemsLabel.setText("Antal varor: " + shoppingCart.getItems().size());
        costLabel.setText("Kostnad: " + String.format("%.2f",shoppingCart.getTotal()));
        
    }
    
    private void updateAccountPanel() {
        
        CreditCard card = model.getCreditCard();
        
        numberTextField.setText(card.getCardNumber());
        nameTextField.setText(card.getHoldersName());
        
        cardTypeCombo.getSelectionModel().select(card.getCardType());
        monthCombo.getSelectionModel().select(""+card.getValidMonth());
        yearCombo.getSelectionModel().select(""+card.getValidYear());

        cvcField.setText(""+card.getVerificationCode());
        
        purchasesLabel.setText(model.getNumberOfOrders()+ " tidigare inköp hos iMat");
        
    }
    
    private void updateCreditCard() {
        
        CreditCard card = model.getCreditCard();
        
        card.setCardNumber(numberTextField.getText());
        card.setHoldersName(nameTextField.getText());
        
        String selectedValue = (String) cardTypeCombo.getSelectionModel().getSelectedItem();
        card.setCardType(selectedValue);
        
        selectedValue = (String) monthCombo.getSelectionModel().getSelectedItem();
        card.setValidMonth(Integer.parseInt(selectedValue));
        
        selectedValue = (String) yearCombo.getSelectionModel().getSelectedItem();
        card.setValidYear(Integer.parseInt(selectedValue));
        
        card.setVerificationCode(Integer.parseInt(cvcField.getText()));

    }
    
    private void setupAccountPane() {
                
        cardTypeCombo.getItems().addAll(model.getCardTypes());
        
        monthCombo.getItems().addAll(model.getMonths());
        
        yearCombo.getItems().addAll(model.getYears());
        
    }*/
}
