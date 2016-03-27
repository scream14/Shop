package shop.product;


import javax.swing.*;
import java.math.BigDecimal;


public class Product {

    private final long ID;
    private String type;
    private String name;
    private BigDecimal price;
    private int count;
    private ImageIcon image;


    public Product(long id, String type, String name, String price, int count, ImageIcon image){
        this.ID = id;
        this.type = type;
        this.name = name;
        this.price = new BigDecimal(price);
        this.count = count;
        this.image = image;
    }
    public Product(Product product){
        this.ID = product.getID();
        this.type = product.getType();
        this.name = product.getName();
        this.price = product.getPrice();
        this.count = product.getCount();
        this.image = product.getImage();
    }


    public long getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public ImageIcon getImage() {
        return image;
    }

}
