package shop.gui.secondary;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;


public class TableForCartPanel extends JPanel {


    private final String[] COLUMN_NAMES = new String[]{"#", "type", "name", "count", "price", "remove"};
    private Object[][] data;
    private DefaultTableModel model;
    private ArrayList<Boolean> checkedToRemove;
    private JTable cartTable;

    private JButton removeFromCartButton;
    private JLabel totalPriceLabel;
    private Cart cart;


    public TableForCartPanel(Cart cart) {

        this.cart = cart;
        checkedToRemove = makeCheckerToRemove();

        initCartTable();
        initRemoveButton();

        totalPriceLabel = new JLabel("Total price: " + cart.getTotalSum() + " $");
        setLayout(new BorderLayout());
        add(new JScrollPane(cartTable));

        add(cartTable.getTableHeader(), BorderLayout.PAGE_START);
        add(cartTable, BorderLayout.CENTER);
        add(totalPriceLabel, BorderLayout.AFTER_LAST_LINE);
        add(removeFromCartButton, BorderLayout.AFTER_LINE_ENDS);

    }

    private void initRemoveButton() {
        removeFromCartButton = new JButton("Remove selected");
        removeFromCartButton.setPreferredSize(removeFromCartButton.getPreferredSize());
        removeFromCartButton.addActionListener(e -> {
            for (int i = cart.getProducts().size() - 1; i >= 0; i--) {
                if (checkedToRemove.get(i)){
                    cart.getProducts().remove(i);
                    model.removeRow(i);
                    checkedToRemove.remove(i);
                }
            }
            makeCheckerToRemove();
            updateTotalPriceLabel();
            totalPriceLabel.repaint();
        });
    }

    private void initCartTable() {
        cartTable = createTable();
        cartTable.setBackground(new Color(162, 255, 161));
        cartTable.setPreferredScrollableViewportSize(cartTable.getPreferredSize());
        cartTable.setFillsViewportHeight(true);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(140);

        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cartTable.getSelectedColumn() == 5) {
                    int selRow = cartTable.getSelectedRow();
                    if (!checkedToRemove.get(selRow)) {
                        checkedToRemove.set(selRow, true);
                    } else if (checkedToRemove.get(selRow)) {
                        checkedToRemove.set(selRow, false);
                    }
                }
            }
        });
    }

    private ArrayList<Boolean> makeCheckerToRemove() {
        ArrayList<Boolean> checked = new ArrayList<>();
        for (int i = 0; i < cart.getProducts().size(); i++) {
            checked.add(Boolean.FALSE);
        }
        return checked;
    }

    private JTable createTable() {
        data = makeDataToTable();
        model = new DefaultTableModel(data, COLUMN_NAMES){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        JTable myTable = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Long.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return Integer.class;
                    case 4:
                        return BigDecimal.class;
                    default:
                        return Boolean.class;
                }
            }
        };
        return myTable;
    }

    private Object[][] makeDataToTable() {
        Object[][] dataFromProducts = new Object[cart.getProducts().size()][COLUMN_NAMES.length];
        for (int i = 0; i < cart.getProducts().size(); i++) {
                dataFromProducts[i] = new Object[]{
                        (i + 1),
                        cart.getProducts().get(i).getType(),
                        cart.getProducts().get(i).getName(),
                        cart.getProducts().get(i).getCount(),
                        cart.getProducts().get(i).getPrice(),
                        Boolean.FALSE
                };
            }
        return dataFromProducts;
    }

    public void updateTotalPriceLabel() {
        totalPriceLabel.setText("Total price: " + cart.getTotalSum() + " $");
    }

}
