package shop.product;

public class Parrot extends ABird{

    public Parrot(int count, double price){
        name = "Parrot";
        this.price = price;
        this.count += count;
    }

}
