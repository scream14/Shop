package shop.gui.secondary;

import shop.product.Product;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;


public class Cart extends JFrame {

    private ArrayList<Product> prodsInCart;

    public Cart(ArrayList<Product> prodsInCart){

        this.prodsInCart = prodsInCart;

        makeTablePanel();
        setLayout(new FlowLayout());
        setLocation(400, 200);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setMinimumSize(new Dimension(getPreferredSize().width,(getPreferredSize().height * 5)));
        pack();

        setVisible(false);
    }

    public BigDecimal getTotalSum() {
        BigDecimal totalPrice = new BigDecimal("0");
        for (Product product : prodsInCart) {
            totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(product.getCount())));
        }
        return totalPrice;
    }

    private void makeTablePanel() {
        TableForCartPanel table = new TableForCartPanel(this);
        add(table);
    }

    public void makeVisible(){
        setVisible(true);
    }

    public ArrayList<Product> getProducts() {
        return prodsInCart;
    }
}
