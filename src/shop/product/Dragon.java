package shop.product;


public class Dragon extends ABird {

    public Dragon(int count, double price){
        name = "Dragon";
        this.price = price;
        this.count += count;
    }

}
