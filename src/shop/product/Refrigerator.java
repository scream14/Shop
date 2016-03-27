package shop.product;


import javax.swing.*;

public class Refrigerator extends Product {

    public Refrigerator(long id, String type, String name, String price, int count, ImageIcon image) {
        super(id, type, name, price, count, image);
    }
    public Refrigerator(Refrigerator refrigerator){
        super(refrigerator);
    }
}
