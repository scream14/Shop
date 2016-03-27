package shop.service;


import java.sql.Date;

public class Transaction {

    private final long ID;
    private Date date;
    private final long CUST_ID;
    private int receipt_id;
    private String totalPrice;


    public Transaction(long id, Date date, long custID, int receipt_id, String totalPrice) {
        this.ID = id;
        this.date = date;
        this.CUST_ID = custID;
        this.receipt_id = receipt_id;
        this.totalPrice = totalPrice;
    }

    public long getID() {
        return ID;
    }

    public Date getDate() {
        return date;
    }

    public long getCUST_ID() {
        return CUST_ID;
    }

    public int getReceipt_id() {
        return receipt_id;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
