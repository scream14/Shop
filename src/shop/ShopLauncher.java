package shop;


import shop.database.DBConnection;
import shop.database.DBSelector;
import shop.gui.ShopFrame;
import shop.service.ShopService;

import java.awt.*;


public class ShopLauncher {
    public static void main(String[] args) throws Exception {

//        -splash:src\shop\splashscreen\sbird.gif
        SplashScreen splash = SplashScreen.getSplashScreen();
        Thread.sleep(3000);
        splash.close();

        DBConnection connection = new DBConnection(DBSelector.MySQL);
        ShopService service = new ShopService(connection.getDbWorker());
        new ShopFrame(service);

    }

}
