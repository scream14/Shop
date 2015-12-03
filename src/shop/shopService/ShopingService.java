package shop.shopService;


public interface ShopingService {

    void buy(int count);
    int left();
    void changePrice(double percent);
    void addNewProduct(String name, double price, int count);
    void addCount(int count);

}
