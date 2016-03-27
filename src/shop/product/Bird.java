package shop.product;


import javax.swing.*;

public class Bird extends Product {

    public Bird(long id, String type, String name, String price, int count, ImageIcon image) {
        super(id, type, name, price, count, image);
    }
    public Bird(Bird bird){
        super(bird);
    }
}
