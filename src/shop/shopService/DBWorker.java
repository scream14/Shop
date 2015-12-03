package shop.shopService;


import com.mysql.fabric.jdbc.FabricMySQLDriver;
import shop.product.*;

import java.sql.*;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;


public class DBWorker {

    private static final String URL = "jdbc:mysql://localhost:3306/myshop";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";


    private ArrayList<AProduct> prodArray;
    private Map<String, String> customers;

    private Connection connection;


    public DBWorker() {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("Connection is OK");
            }
            prodArray = new ArrayList<>();
            customers = new HashMap<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM birds");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                switch (name) {
                    case "Chicken":
                        Chicken chicken = new Chicken(resultSet.getInt("count"), resultSet.getDouble("price"));
                        prodArray.add(chicken);
                        break;
                    case "Dragon":
                        Dragon dragon = new Dragon(resultSet.getInt("count"), resultSet.getDouble("price"));
                        prodArray.add(dragon);
                        break;
                    case "Duck":
                        Duck duck = new Duck(resultSet.getInt("count"), resultSet.getDouble("price"));
                        prodArray.add(duck);
                        break;
                    case "Eagle":
                        Eagle eagle = new Eagle(resultSet.getInt("count"), resultSet.getDouble("price"));
                        prodArray.add(eagle);
                        break;
                    case "Parrot":
                        Parrot parrot = new Parrot(resultSet.getInt("count"), resultSet.getDouble("price"));
                        prodArray.add(parrot);
                        break;
                    case "Penguin":
                        Penguin penguin = new Penguin(resultSet.getInt("count"), resultSet.getDouble("price"));
                        prodArray.add(penguin);
                        break;
                    default:
                        System.out.println("default case in switch: " + name);
                }

            }
            resultSet = statement.executeQuery("SELECT * FROM customer");
            while (resultSet.next()) {
                customers.put(resultSet.getString("phone"), resultSet.getString("name"));

            }
            System.out.println(customers.size() + " size customers");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeUpdate(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCustomerIdFromDB(String phone) {
        int idCust = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cust_id from customer WHERE phone='" + phone + "'");

            while (resultSet.next()) {
                idCust = resultSet.getInt("cust_id");
                System.out.println("cust_id = " + idCust);
                return idCust;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCust;
    }
    public String[] getTransactionInfo() {
        String[] transInfo = new String[6];
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM myshop.transaction");

            while (resultSet.next()) {
                transInfo[0] = String.valueOf(resultSet.getInt("Tran_ID"));
                transInfo[1] = String.valueOf(resultSet.getDate("Date"));
                transInfo[2] = String.valueOf(resultSet.getString("cust_id"));
                transInfo[3] = resultSet.getString("Type");
                transInfo[4] = String.valueOf(resultSet.getInt("Count"));
                transInfo[5] = String.valueOf(resultSet.getDouble("Total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transInfo;
    }
    public boolean checkCustomers(String phone, String name) {
        try {
            if (customers.containsKey(phone)) {
                System.out.println(phone + " exist in db");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        customers.put(phone, name);
        System.out.println(customers.size() + " size customers");
        return true;
    }

    public String[][] getTransHistory() {
        String[][] transInfo = new String[getCountOfRowInTable("transaction")][ 6];
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM myshop.transaction");


            while (resultSet.next()) {
                    transInfo[resultSet.getRow() - 1][0]=String.valueOf(resultSet.getInt("Tran_ID"));
                    transInfo[resultSet.getRow() - 1][1]=String.valueOf(resultSet.getDate("Date"));
                    transInfo[resultSet.getRow() - 1][2]=String.valueOf(resultSet.getString("cust_id"));
                    transInfo[resultSet.getRow() - 1][3]=resultSet.getString("Type");
                    transInfo[resultSet.getRow() - 1][4]=String.valueOf(resultSet.getInt("Count"));
                    transInfo[resultSet.getRow() - 1][5]=String.valueOf(resultSet.getDouble("Total"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transInfo;
    }
    private int getCountOfRowInTable(String tableName){
        int count = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM myshop." + tableName + ";");

            while (resultSet.next()) {
                count = resultSet.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    public ArrayList<AProduct> getProdArray() {
        return prodArray;
    }

    public Connection getConnection() {
        return connection;
    }

    public Map<String, String> getCustomers() {
        return customers;
    }


}
