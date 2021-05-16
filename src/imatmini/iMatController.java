/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

//import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import se.chalmers.cse.dat216.project.*;


/**
 *
 * @author oloft
 */
public class iMatController implements Initializable, ShoppingCartListener {

    // For switching scenes
    SceneController sceneController = new SceneController();
    Order currentOrder;

    @FXML
    private void switchToKassa1(ActionEvent event) throws IOException {
        sceneController.switchToKassa1(event);
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

    @FXML private Label dNamn;
    @FXML private ImageView dBild;
    @FXML private Label dPris;

    public void populateDetalvy(Product product){
        dNamn.setText(product.getName());
        dBild.setImage(model.getImage(product, 147, 102));
        dPris.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
    }

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
    }

    public void updateVarukorg(List<ShoppingItem> productList) {
        varukorgFlowPane.getChildren().clear();

        for (ShoppingItem product : productList) {

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
        titelLabel.setText("Sökord: " + sökruta.getText());
        List<Product> matches = model.findProducts(sökruta.getText());
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
