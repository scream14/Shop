package shop.product;


public class Penguin extends ABird {

    public Penguin(int count, double price){
        name = "Penguin";
        this.price = price;
        this.count += count;
    }

}
