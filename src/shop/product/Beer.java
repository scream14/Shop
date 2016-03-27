package shop.product;

import javax.swing.*;


public class Beer extends Product {

    public Beer(long id, String type, String name, String price, int count, ImageIcon image) {
        super(id, type, name, price, count, image);
    }
    public Beer (Beer beer){
        super(beer);
    }
}
