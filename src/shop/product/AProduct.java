package shop.product;

import shop.shopService.ShopingService;


public abstract class AProduct implements ShopingService{

    protected String name;
    protected double price;
    protected int count;


    public AProduct(){

    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
