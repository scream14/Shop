package shop.gui;


import shop.product.Product;
import shop.service.Customer;
import shop.service.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


public class CustomersPanel extends JPanel {


    private final String[] COLUMN_NAMES = new String[]{"#", "name", "phone", "sum of all purchases, $"};
    private ArrayList<Customer> customers;
    private ArrayList<Transaction> transactions;


    public CustomersPanel(ArrayList<Customer> customers, ArrayList<Transaction> transactions) {

        this.customers = customers;
        this.transactions = transactions;

        JTable myCustomersTable = makeMyTable();
        myCustomersTable.setBackground(new Color(0, 255, 53));
        myCustomersTable.setPreferredScrollableViewportSize(myCustomersTable.getPreferredSize());
        myCustomersTable.setFillsViewportHeight(true);
        myCustomersTable.getColumnModel().getColumn(1).setPreferredWidth(140);

        setLayout(new BorderLayout());
        add(new JScrollPane(myCustomersTable));
        add(myCustomersTable.getTableHeader(), BorderLayout.PAGE_START);
        add(myCustomersTable, BorderLayout.CENTER);
    }

    private JTable makeMyTable() {
        Object[][] data = makeDataToTable();
        DefaultTableModel model = new DefaultTableModel(data, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable myTable = new JTable(model){
            public String getToolTipText(MouseEvent event) {
                String tip = "<html>";
                java.awt.Point p = event.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);
                try {
                    if (realColumnIndex == 3) {
                        tip += "<p>For more details look transactions:</p><h3>";
                        for (Transaction transaction : transactions) {
                            if (transaction.getCUST_ID() == (Long) getValueAt(rowIndex, 0)) {
                                tip += transaction.getID() + ", ";
                            }
                        }
                        tip = tip.substring(0, (tip.length() - 2));
                        tip += "</h3>";
                    }
                    tip += "</html>";
                    repaint();
                    return tip;
                } catch (ArrayIndexOutOfBoundsException ex) {
//                    ignore
                }
                return tip += "</html>";
            }
        };
        return myTable;
    }

    private Object[][] makeDataToTable() {
        Object[][] dataFromArrayList = new Object[customers.size()][COLUMN_NAMES.length];
        for (int i = 0; i < customers.size(); i++) {
            dataFromArrayList[i] = new Object[]{
                    customers.get(i).getID(),
                    customers.get(i).getName(),
                    customers.get(i).getPhone(),
                    totalSumFromTransaction(customers.get(i))
            };
        }
        return dataFromArrayList;
    }

    private BigDecimal totalSumFromTransaction(Customer customer) {
        BigDecimal totalSum = new BigDecimal("0");
        for (Transaction transaction : transactions){
            if(transaction.getCUST_ID() == customer.getID()){
                totalSum = totalSum.add(new BigDecimal(transaction.getTotalPrice()));
            }
        }
        return totalSum.setScale(2, RoundingMode.CEILING);

    }

}
