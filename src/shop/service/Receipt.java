package shop.service;


import java.math.BigDecimal;


public class Receipt {


    private int receipt_id;
    private int prod_id;
    private int count;
    private BigDecimal price;


    public Receipt(int receipt_id, int prod_id, int count, String price){
        this.receipt_id = receipt_id;
        this.prod_id = prod_id;
        this.count = count;
        this.price = new BigDecimal(price);
    }

    public int getReceipt_id() {
        return receipt_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
