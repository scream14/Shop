package shop.product;


public class Duck extends ABird {

    public Duck(int count, double price){
        name = "Duck";
        this.price = price;
        this.count += count;
    }

}
