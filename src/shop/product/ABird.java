package shop.product;


import shop.ShopLauncher;

import java.sql.SQLException;
import java.text.DecimalFormat;


public abstract class ABird extends AProduct {

    @Override
    public void buy(int count) {

        this.setCount(this.left() - count);
        try {
            ShopLauncher.getDBWorker().makeUpdate("UPDATE myshop.birds SET count= " + this.getCount() + " WHERE name=" + "'" + this.getName() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int left() {
        return this.getCount();
    }

    @Override
    public void changePrice(double percent) {

        double newPrice = this.getPrice() + (this.getPrice()*percent/100);
        new DecimalFormat("#.##").format(newPrice);
        this.setPrice(newPrice);
    }

    @Override
    public void addNewProduct(String name, double price, int count) {

    }

    @Override
    public void addCount(int count) {
        this.setCount(this.getCount() + count);
    }
}
