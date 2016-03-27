package shop.database;


import shop.product.*;
import shop.service.Customer;
import shop.service.Receipt;
import shop.service.Transaction;

import javax.swing.ImageIcon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DBWorker {


    private final static String INSERT_NEW_PRODUCT =
            "INSERT INTO myshop3.products (type, name, image, count, price)  VALUES(?, ?, ?, ?, ?)";
    private final static String INSERT_NEW_RECEIPTS =
            "INSERT INTO myshop3.receipts (receipt_id, prod_id, count, price) VALUES(?, ?, ?, ?)";
    private final static String INSERT_NEW_TRANSACTION =
            "INSERT INTO myshop3.transactions (date, cust_id, receipt_id, total_price) VALUES(?, ?, ?, ?)";

    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    private ArrayList<Product> products;
    private ArrayList<Receipt> receipts;
    private ArrayList<Customer> customers;
    private ArrayList<Transaction> transactions;


    public DBWorker(Connection connection) {
        this.connection = connection;
        products = new ArrayList<>();
        receipts = new ArrayList<>();
        customers = new ArrayList<>();
        transactions = new ArrayList<>();
    }


    public void loadData() {
        loadProductsFromDB();
        loadReceiptsFromDB();
        loadCustomersFromDB();
        loadTransactionsFromDB();
    }

    public int getLastReceiptID() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(receipt_id) AS lastID FROM receipts");
            while (resultSet.next()) {
                return resultSet.getInt("lastID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.err.println("some problem in getLastReceiptID");
        return 1;
    }

    public void addReceiptToDB(int receipt_id, int prod_id, int count, String price) {
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_RECEIPTS);

            preparedStatement.setInt(1, receipt_id);
            preparedStatement.setInt(2, prod_id);
            preparedStatement.setInt(3, count);
            preparedStatement.setString(4, price);

            preparedStatement.execute();
            preparedStatement.clearParameters();

            loadReceiptsFromDB();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCustomer(Customer customer) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO myshop3.customers (name, phone) VALUES ('" +
                            customer.getName() + "', '" +
                            customer.getPhone() + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                loadCustomersFromDB();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTransaction(String customerPhone, String totalSum) {
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_TRANSACTION);

            preparedStatement.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
            preparedStatement.setInt(2, getCustIdByPhone(customerPhone));
            preparedStatement.setInt(3, getLastReceiptID());
            preparedStatement.setString(4, totalSum);

            preparedStatement.execute();
            preparedStatement.clearParameters();

            loadTransactionsFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProductsFromDB() {
        products.clear();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM products");
            if (!resultSet.next()) {
                loadTestData();
            }
            resultSet.beforeFirst();
            while (resultSet.next()) {
                Product prod = new Product(
                        resultSet.getLong("prod_id"),
                        resultSet.getString("type"),
                        resultSet.getString("name"),
                        resultSet.getString("price"),
                        resultSet.getInt("count"),
                        new ImageIcon(resultSet.getBlob("image").getBytes(1L, (int) resultSet.getBlob("image").length())));
                products.add(prod);
            }
            System.out.println("Products loaded from db" + " (rows: " + products.size() + ")");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadReceiptsFromDB() {
        receipts.clear();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM receipts");
            resultSet.beforeFirst();
            while (resultSet.next()) {
                Receipt receipt = new Receipt(
                        resultSet.getInt("receipt_id"),
                        resultSet.getInt("prod_id"),
                        resultSet.getInt("count"),
                        resultSet.getString("price"));
                receipts.add(receipt);
            }
            System.out.println("Receipts loaded from db" + " (rows: " + receipts.size() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCustomersFromDB() {
        customers.clear();
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM customers");

            while (resultSet.next()) {
                customers.add(
                        new Customer(resultSet.getInt("cust_id"),
                                resultSet.getString("name"),
                                resultSet.getString("phone")));
            }
            System.out.println("Customers loaded from db" + " (rows: " + customers.size() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void loadTransactionsFromDB() {
        transactions.clear();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM transactions");

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("tran_id"),
                        resultSet.getDate("date"),
                        resultSet.getInt("cust_id"),
                        resultSet.getInt("receipt_id"),
                        resultSet.getString("total_price")
                ));
            }
            System.out.println("Transactions loaded from db" + " (rows: " + transactions.size() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loadTestData() {
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_PRODUCT);

            preparedStatement.setString(1, Bird.class.getSimpleName());
            preparedStatement.setString(2, "Eagle");
            preparedStatement.setBlob(3, new FileInputStream("bs/eagle.png"));
            preparedStatement.setInt(4, 20);
            preparedStatement.setString(5, "55");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Bird.class.getSimpleName());
            preparedStatement.setString(2, "Parrot");
            preparedStatement.setBlob(3, new FileInputStream("bs/parrot.png"));
            preparedStatement.setInt(4, 30);
            preparedStatement.setString(5, "40");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Bird.class.getSimpleName());
            preparedStatement.setString(2, "Duck");
            preparedStatement.setBlob(3, new FileInputStream("bs/duck.png"));
            preparedStatement.setInt(4, 50);
            preparedStatement.setString(5, "17.5");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Beer.class.getSimpleName());
            preparedStatement.setString(2, "Heineken");
            preparedStatement.setBlob(3, new FileInputStream("bs/beer.png"));
            preparedStatement.setInt(4, 70);
            preparedStatement.setString(5, "2.99");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Beer.class.getSimpleName());
            preparedStatement.setString(2, "Corona Extra");
            preparedStatement.setBlob(3, new FileInputStream("bs/beer1.png"));
            preparedStatement.setInt(4, 60);
            preparedStatement.setString(5, "2.59");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Beer.class.getSimpleName());
            preparedStatement.setString(2, "Budweiser");
            preparedStatement.setBlob(3, new FileInputStream("bs/beer2.png"));
            preparedStatement.setInt(4, 50);
            preparedStatement.setString(5, "1.99");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Refrigerator.class.getSimpleName());
            preparedStatement.setString(2, "Samsung RF007");
            preparedStatement.setBlob(3, new FileInputStream("bs/samsung.png"));
            preparedStatement.setInt(4, 4);
            preparedStatement.setString(5, "359");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Refrigerator.class.getSimpleName());
            preparedStatement.setString(2, "Liebherr SBSes 300");
            preparedStatement.setBlob(3, new FileInputStream("bs/LG.png"));
            preparedStatement.setInt(4, 2);
            preparedStatement.setString(5, "970");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            preparedStatement.setString(1, Refrigerator.class.getSimpleName());
            preparedStatement.setString(2, "Bosch KGN 69");
            preparedStatement.setBlob(3, new FileInputStream("bs/philips.png"));
            preparedStatement.setInt(4, 7);
            preparedStatement.setString(5, "550");

            preparedStatement.execute();
            preparedStatement.clearParameters();

            System.err.println("loaded test data to DB");

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public int getCustIdByPhone(String custPhone) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT cust_id FROM myshop3.customers WHERE phone = '" + custPhone + "'");
            while (resultSet.next()) {
                return resultSet.getInt("cust_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.err.println("some problem in getCustIdByPhone");
        return 1;
    }

    public void updateCount(long id, int count) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    "UPDATE myshop3.products SET count = " + count + " WHERE prod_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                loadProductsFromDB();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // this method take correct data
    public void updatePriceInDB(Product product) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    "UPDATE myshop3.products SET price = '" + product.getPrice() + "' " +
                            "WHERE type = '" + product.getType() + "'AND name = '" + product.getName() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                loadProductsFromDB();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addNewProduct(String type, String name, String path, int count, String price) {
        try {
            preparedStatement = connection.prepareStatement(INSERT_NEW_PRODUCT);

            preparedStatement.setString(1, type);
            preparedStatement.setString(2, name);
            preparedStatement.setBlob(3, new FileInputStream(path));
            preparedStatement.setInt(4, count);
            preparedStatement.setString(5, price);

            preparedStatement.execute();

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                loadProductsFromDB();
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


