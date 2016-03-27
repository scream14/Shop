package shop.database;


import com.mysql.fabric.jdbc.FabricMySQLDriver;
import java.sql.*;



public class DBConnection {


    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String URL_DERBY = "jdbc:derby://localhost:1527/myShopDerby;create=true";

    private static final String URL_MySQL = "jdbc:mysql://localhost:3306/myshop3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private DBWorker dbWorker;
    private Connection connection = null;


    public DBConnection(DBSelector db){
        if (db.equals(DBSelector.MySQL)){
            dbMySQL();
        } else {
            dbDerby();
        }
    }


    private void dbDerby() {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
            connection = DriverManager.getConnection(URL_DERBY);

            if (!connection.isClosed()){
                System.out.println("Connection Derby is OK");
            }
            loadData();
        } catch ( SQLException e){
            e.printStackTrace();
        }
    }

    private void dbMySQL() {
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL_MySQL, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("Connection with MySQL is OK");
            }
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        dbWorker = new DBWorker(connection);
        dbWorker.loadData();
    }

    public DBWorker getDbWorker() {
        return dbWorker;
    }

}
