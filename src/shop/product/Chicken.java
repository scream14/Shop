package shop.product;

public class Chicken extends ABird {

    public Chicken(int count, double price) {
        name = "Chicken";
        this.price = price;
        this.count += count;
    }
}
