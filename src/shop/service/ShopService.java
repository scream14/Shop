package shop.service;


import shop.database.DBWorker;
import shop.product.Product;


import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


public class ShopService {


    private DBWorker dbWorker;


    public ShopService(DBWorker dbWorker) {
        this.dbWorker = dbWorker;
    }


    public void newPrice(Product product, String price) {
        product.setPrice(new BigDecimal(price).setScale(2, RoundingMode.CEILING));
        dbWorker.updatePriceInDB(product);
    }

    public void updatePriceByPercent(Product product, String percent) {
        product.setPrice((product.getPrice().add(
                product.getPrice().multiply(new BigDecimal("0.01")).multiply(new BigDecimal(percent))))
                .setScale(2, RoundingMode.CEILING));
        dbWorker.updatePriceInDB(product);
    }

    public void addNewProduct(String type, String name, String path, int count, String price) {

        dbWorker.addNewProduct(type, name, path, count, price);
    }


    public String buyProducts(Customer customer, ArrayList<Product> products) {
        String report = checkCustomer(customer);
        generateReceiptAndUpdateCount(products);
        dbWorker.addTransaction(customer.getPhone(), getTotalSumOfTransaction(products).toString());
        return report;
    }

    private String checkCustomer(Customer customer) {
        String massage = "Buy is successful! ";
        if (isCustomerExist(customer)) {
            massage = "We happy to see you again, " + customer.getName() + "! ";
        } else {
            dbWorker.addCustomer(customer);
            massage += "Be happy new customer " + customer.getName() + "! ";
        }
        return massage;
    }

    private void generateReceiptAndUpdateCount(ArrayList<Product> products) {
        int nextReceiptInDB = dbWorker.getLastReceiptID() + 1;
        for (int i = 0; i < products.size(); i++) {
            dbWorker.addReceiptToDB(
                    nextReceiptInDB,
                    (int) products.get(i).getID(),
                    products.get(i).getCount(),
                    products.get(i).getPrice().toString());
            dbWorker.updateCount(products.get(i).getID(), getCountFromDB(products.get(i).getID()));
        }
    }

    private int getCountFromDB(long inCartId) {
        for (Product product : dbWorker.getProducts()) {
            if (product.getID() == inCartId) {
                return product.getCount();
            }
        }
        return 0;
    }

    private BigDecimal getTotalSumOfTransaction(ArrayList<Product> products) {
        BigDecimal totalSum = new BigDecimal("0");
        for (Product product : products) {
            totalSum = totalSum.add(product.getPrice().multiply(new BigDecimal(product.getCount())));
        }
        return totalSum.setScale(2, RoundingMode.CEILING);
    }

    private boolean isCustomerExist(Customer customer) {
        for (Customer cus : dbWorker.getCustomers()) {
            if (cus.getPhone().equals(customer.getPhone())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPriceCorrect(String price) {
        try {
            return new BigDecimal(price).compareTo(BigDecimal.ZERO) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateCount(Product product, int addition) {
        product.addCount(addition);
        dbWorker.updateCount(product.getID(), product.getCount());
    }

    public ArrayList<Customer> getCustomers() {
        return dbWorker.getCustomers();
    }

    public ArrayList<Product> getProducts() {
        return dbWorker.getProducts();
    }

    public ArrayList<Transaction> getTransactions() {
        return dbWorker.getTransactions();
    }

    public ArrayList<Receipt> getReceipts() {
        return dbWorker.getReceipts();
    }

    public boolean checkNewProdType(String type) {
        if (!type.isEmpty()) {
            char[] typeChars = type.toCharArray();
            for (char ch : typeChars) {
                if (!Character.isLetter(ch)) {
                    if (Character.isSpaceChar(ch)) {
                        break;
                    }
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    public boolean checkNewProdName(String name) {
        if (!name.isEmpty()) {
            char[] nameChars = name.toCharArray();
            for (char ch : nameChars) {
                if (!Character.isLetterOrDigit(ch)) {
                    if (Character.isSpaceChar(ch)) {
                        break;
                    }
                    return false;
                }
            }
            for (Product product : dbWorker.getProducts()) {
                if (product.getName().equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isProductExist(String type, String name) {
        for (Product product : dbWorker.getProducts()) {
            if (product.getType().equals(type) && product.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewProdIconPathExist(String iconPath) {
        if (!iconPath.isEmpty()){
            if (new File(iconPath).exists()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkNewProdCount(String count) {
        try {
            Integer.parseInt(count);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean checkNewProdPrice(String newProdPrice) {
        try {
            return (new BigDecimal(newProdPrice).compareTo(BigDecimal.ZERO)) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
