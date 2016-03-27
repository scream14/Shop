package shop.gui;


import shop.product.Product;
import shop.service.Customer;
import shop.service.Receipt;
import shop.service.ShopService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;


public class TransactionsPanel extends JPanel {


    private final String[] COLUMN_NAMES = new String[]{"#", "date", "customer_id", "receipt_id", "total price, $"};
    private ShopService service;


    public TransactionsPanel(ShopService service) {

        this.service = service;

        JTable myTransactionsTable = makeMyTable();
        myTransactionsTable.setBackground(new Color(255, 209, 43));
        myTransactionsTable.setPreferredScrollableViewportSize(myTransactionsTable.getPreferredSize());
        myTransactionsTable.setFillsViewportHeight(true);
        myTransactionsTable.getColumnModel().getColumn(1).setPreferredWidth(140);

        setLayout(new BorderLayout());

        add(new JScrollPane(myTransactionsTable));
        add(myTransactionsTable.getTableHeader(), BorderLayout.PAGE_START);
        add(myTransactionsTable, BorderLayout.CENTER);
    }

    private JTable makeMyTable() {
        Object[][] data = makeDataToTable();
        DefaultTableModel model = new DefaultTableModel(data, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable myTable = new JTable(model) {
            public String getToolTipText(MouseEvent event) {
                String tip = "<html>";
                java.awt.Point p = event.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);
                try {
                    if (realColumnIndex == 3) {
                        tip += "<h1>Receipt # " + getValueAt(rowIndex, colIndex) + "</h1>";
                        for (Receipt receipt : service.getReceipts()) {
                            if (receipt.getReceipt_id() == (Integer) getValueAt(rowIndex, colIndex)) {
                                tip += "<p>" + receipt.getCount() + "  -  " +
                                        getProdName(receipt.getProd_id()) + "  -  " +
                                        receipt.getPrice() + " $/pcs" + "</p>";
                            }
                        }
                    } else if (realColumnIndex == 2) {
                        for (Customer customer : service.getCustomers()) {
                            if (customer.getID() == (Long) getValueAt(rowIndex, colIndex)) {
                                tip += "<p>Name: " + customer.getName() + "</p>";
                                tip += "<p>Phone: " + customer.getPhone() + "</p>";
                            }
                        }
                    }
                    tip += "</html>";
                    repaint();
                    return tip;
                } catch (ArrayIndexOutOfBoundsException ex) {
//                    ignore
                }
                return tip += "</html>";
            }

            private String getProdName(long prod_id) {
                String prodName = "";
                for (Product product : service.getProducts()){
                    if (product.getID() == prod_id){
                        prodName = product.getType() + " /";
                        prodName += product.getName();
                    }
                }
                return prodName;
            }
        };
        return myTable;
    }

    private Object[][] makeDataToTable() {
        Object[][] dataFromArrayList = new Object[service.getTransactions().size()][COLUMN_NAMES.length];
        for (int i = 0; i < service.getTransactions().size(); i++) {
            dataFromArrayList[i] = new Object[]{
                    service.getTransactions().get(i).getID(),
                    service.getTransactions().get(i).getDate(),
                    service.getTransactions().get(i).getCUST_ID(),
                    service.getTransactions().get(i).getReceipt_id(),
                    service.getTransactions().get(i).getTotalPrice()
            };
        }
        return dataFromArrayList;
    }
}


