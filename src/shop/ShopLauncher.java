package shop;


import shop.shopGUI.ShopWindow;
import shop.shopService.DBWorker;


import java.io.IOException;


public class ShopLauncher {

    static DBWorker worker = new DBWorker();


    public static void main(String[] args) throws IOException {

        -splash:bs/sbird.gif
        SplashScreen splash = SplashScreen.getSplashScreen();
        Thread.sleep(5000);
        splash.close();

        ShopWindow sw = new ShopWindow(worker);
    }
    public static DBWorker getDBWorker(){
        return worker;
    }
}
