/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imatmini;

/**
 *
 * @author oloft
 */
import java.util.*;

import javafx.scene.image.Image;
import se.chalmers.cse.dat216.project.*;


/**
 * Wrapper around the IMatDataHandler. The idea is that it might be useful to
 * add an extra layer that can provide special features
 *
 */
public class Model {

    private static Model instance = null;
    private IMatDataHandler iMatDataHandler;

     private final ArrayList<String> availableCardTypes = new ArrayList<String>(Arrays.asList("MasterCard", "Visa"));
     private final ArrayList<String> months = new ArrayList<String>(Arrays.asList("01", "02","03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
     private final ArrayList<String> years = new ArrayList<String>(Arrays.asList("21", "22", "23", "24", "25", "26", "27"));
    /**
     * Constructor that should never be called, use getInstance() instead.
     */
    protected Model() {
        // Exists only to defeat instantiation.
    }

    private Map<String, Card> cardMap = new HashMap();

    public Map<String, Card> getCardMap() {
        return cardMap;
    }

    public boolean isFirstRun(){
       return iMatDataHandler.isFirstRun();
    }

    public List<Product> getGilladeVaror(){
        return iMatDataHandler.favorites();
    }
    /**
     * Returns the single instance of the Model class.
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
            instance.init();
        }
        return instance;
    }

    public void gillaVara(Product product){
        iMatDataHandler.addFavorite(product);
    }

    public void oGillaVara(Product product){
        iMatDataHandler.removeFavorite(product);
    }

    public boolean isGillad(Product product){
        return iMatDataHandler.isFavorite(product);
    }

    private void init() {

        iMatDataHandler = IMatDataHandler.getInstance();

    }

    public List<Product> getProducts() {
        return iMatDataHandler.getProducts();
    }

    public Product getProduct(int idNbr) {
        return iMatDataHandler.getProduct(idNbr);
    }
    
    public List<Product> findProducts(java.lang.String s) {
        return iMatDataHandler.findProducts(s);
    }

    public Image getImage(Product p) {
        return iMatDataHandler.getFXImage(p);
    }

    public Image getImage(Product p, double width, double height) {
        return iMatDataHandler.getFXImage(p, width, height);
    }

    public void addToShoppingCart(Product p) {
        ShoppingCart shoppingCart = iMatDataHandler.getShoppingCart();

        ShoppingItem item = new ShoppingItem(p);
        Model.getInstance().getShoppingCart().addItem(item);
        
        //shoppingCart.addProduct(p);
    }

    public List<String> getCardTypes() {
        return availableCardTypes;
    }
    
    public List<String> getMonths() {
        return months;
    }
    
    public List<String> getYears() {
        return years;
    }
    
    public CreditCard getCreditCard() {
        return iMatDataHandler.getCreditCard();
    }
    
    public Customer getCustomer() {
        return iMatDataHandler.getCustomer();
    }

    public ShoppingCart getShoppingCart() {
        return iMatDataHandler.getShoppingCart();
    }

    public void clearShoppingCart() {

        iMatDataHandler.getShoppingCart().clear();

    }

    public Order placeOrder() { return iMatDataHandler.placeOrder(); }
    public java.util.List<Order> getOrders() { return iMatDataHandler.getOrders(); }

    public int getNumberOfOrders() {
        return iMatDataHandler.getOrders().size();
    }

    public void shutDown() {
        iMatDataHandler.shutDown();
    }
}
