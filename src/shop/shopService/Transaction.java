package shop.shopService;



import shop.ShopLauncher;
import shop.product.AProduct;

import java.util.ArrayList;
import java.util.Map;


public class Transaction {

    private Map<String, String> customers;
    private ArrayList<AProduct> prods;

    private final String[] COLUMN_NAMES = new String[]{"id","Date","Customer","Type","Count","Total, $"};
    private String[][] birdsInfo = ShopLauncher.getDBWorker().getTransHistory();

    public Transaction(ArrayList<AProduct> prods){
        this.prods = prods;
    }

    public void addInfo(String[] trans){
        String[][]copy = new String[birdsInfo.length + 1][];
        if (birdsInfo.length != 0){
            for (int i = 0; i<birdsInfo.length; i++){
                copy[i] = birdsInfo[i];
            }
            copy[copy.length - 1] = trans;
            birdsInfo = copy;
        } else {
            birdsInfo = copy;
            birdsInfo[0] = trans;
    }
        for (int i = 0; i<birdsInfo.length; i++){
            for (int k = 0; k<birdsInfo[i].length; k++){
                System.out.print(birdsInfo[i][k]);
            }
            System.out.println();
        }
    }

    public String[][] getBirdsInfo() {

        return birdsInfo;
    }

    public String[] getCOLUMN_NAMES() {
        return COLUMN_NAMES;
    }
}


