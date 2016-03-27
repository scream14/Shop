package shop.service;


public class Customer {


    private final long ID;
    private String name;
    private String phone;


    public Customer(long id, String name, String phone) {
        this.ID = id;
        this.name = name;
        this.phone = phone;
    }

    public Customer(String name, String phone){
        // it's ID not impotent
        ID = 0;
        this.name = name;
        this.phone = phone;
    }


    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
