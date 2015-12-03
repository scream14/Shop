package shop.shopGUI;

import shop.ShopLauncher;
import shop.product.AProduct;
import shop.shopService.DBWorker;
import shop.shopService.Transaction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ShopWindow extends JPanel implements ActionListener {
    ImageIcon[] images;
    JFrame f = new JFrame("Birds shop");
    JPanel windowShop;
    JPanel windowTransaction;
    JPanel buttonPanel;
    JMenuBar menuBar;

    JLabel countLabel;
    JLabel imageLabel;
    JLabel priceLabel;
    JLabel customerName;
    JLabel customerPhoneNumber;

    JButton addBirds;
    JButton removeBirds;
    JButton buy;

    JComboBox typeCBox;
    JTextField nameField;
    JTextField phoneField;
    Transaction trans;
    int toBuy;
//    int id = 1;

    public ShopWindow(DBWorker worker) throws IOException {
        trans = new Transaction(worker.getProdArray());

        // menu
        menuBar = new JMenuBar();
        JMenu general = getJMenu();
        menuBar.add(general);

        // combobox
        typeCBox = getComboBox(worker.getProdArray());

        // labels
        countLabel = new JLabel("Quantity: " + toBuy);
        imageLabel = new JLabel(createImageIcon("bs/board.png"));
        priceLabel = new JLabel("     by one: " + getSUM() + " $");
        customerName = new JLabel("Name");
        customerPhoneNumber = new JLabel("Phone (12 digits)");

        // buttons
        addBirds = new JButton("More");
        removeBirds = new JButton("Less");
        buy = new JButton("Buy");

        nameField = new JTextField();
        phoneField = new JTextField();

        nameField.setLocation(200, 200);
        nameField.setPreferredSize(new Dimension(20, 80));

        GridBagLayout gb = new GridBagLayout();
        f.setLayout(gb);

        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(204, 255, 27));
        buttonPanel.add(buy);
        buttonPanel.add(addBirds);
        buttonPanel.add(removeBirds);

        f.setLocation(300, 100);
        f.setMinimumSize(new Dimension(600, 600));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buy.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {

                boolean flag = true;

                if (nameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please, enter your NAME");
                    flag = false;
                } else if (phoneField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(f, "Please, enter your PHONE");
                    flag = false;
                } else if (phoneField.getText().length() != 12) {
                    JOptionPane.showMessageDialog(f, "Please, enter 12 digits of your phone");
                    flag = false;

                } else if (phoneField.getText().length() == 12) {
                    String phone = phoneField.getText();
                    char[] digits = phone.toCharArray();
                    for (char ch : digits) {
                        if (!Character.isDigit(ch)) {
                            JOptionPane.showMessageDialog(f, "Incorrect phone number");
                            flag = false;
                        }
                    }
                } else if (toBuy == 0) {
                    JOptionPane.showMessageDialog(f, "You can't buy 0 bird");
                    flag = false;
                } else if (flag) {
                    for (AProduct prod : worker.getProdArray()) {
                        if (typeCBox.getSelectedItem().equals(prod.getName())) {
                            try {
                                if (worker.checkCustomers(phoneField.getText(), nameField.getText())) {
                                    System.out.println("[add to db new customer]");
                                    ShopLauncher.getDBWorker().makeUpdate("INSERT INTO customer (name, phone) values ('"
                                            + nameField.getText() + "', '" + phoneField.getText() + "');");
                                }
                                ShopLauncher.getDBWorker().makeUpdate("INSERT INTO " +
                                        "transaction (Date, cust_id, Type, Count, Total) VALUES ('" + getDateToDB() +
                                        "'," + worker.getCustomerIdFromDB(phoneField.getText()) + ",'" + prod.getName()
                                        + "'," + toBuy + "," + (prod.getPrice() * toBuy) + ")");
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            String[] tranInfo = worker.getTransactionInfo();
                            trans.addInfo(tranInfo);
                            prod.buy(toBuy);
                            toBuy = prod.left();
                            updateNumber();
                            f.repaint();
                            System.out.println("You bought " + toBuy + " " + prod.getName());
                        }
                    }

                }
            }
        });
        addBirds.addActionListener(e -> {
            for (int i = 0; i < worker.getProdArray().size(); i++) {
                if (typeCBox.getSelectedItem().equals(worker.getProdArray().get(i).getName())) {
                    if (toBuy == worker.getProdArray().get(i).left()) {
                        --toBuy;
                    }

                }

            }
            ++toBuy;
            updateNumber();

        });
        removeBirds.addActionListener(e -> {
            --toBuy;
            if (toBuy <= 0) {
                toBuy = 0;
            }
            updateNumber();
        });

        f.setJMenuBar(menuBar);

        windowShop = getWindowShop();
        windowTransaction = getWindowTransaction();

        f.add(windowShop);
        f.add(windowTransaction);

        f.pack();
        f.setVisible(true);
        f.setResizable(false);
        repaint();

    }


    private JPanel getWindowTransaction() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 500));

        JTable transaction = new JTable(trans.getBirdsInfo(), trans.getCOLUMN_NAMES());
        BorderLayout bl = new BorderLayout();
        panel.setLayout(bl);
        transaction.setFillsViewportHeight(true);
        transaction.setBackground(new Color(162, 255, 161));
        JScrollPane sp = new JScrollPane(transaction);
        transaction.setFillsViewportHeight(true);
        transaction.getColumnModel().getColumn(1).setPreferredWidth(140);
        JLabel label = new JLabel("Total sold: " + getSUM());

        panel.setLayout(new BorderLayout());
        panel.add(transaction.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(transaction, BorderLayout.CENTER);
        panel.add(label, BorderLayout.PAGE_END);

        panel.setVisible(false);
        return panel;
    }

    private JPanel getWindowShop() {
        JPanel pws = new JPanel();
        pws.setPreferredSize(new Dimension(600, 600));
        GridBagLayout gb = new GridBagLayout();
        pws.setLayout(gb);
        pws.add(infoAboutBuyer(customerName, customerPhoneNumber, nameField, phoneField), getGBConstraints(0, 0, 1, 1, GridBagConstraints.SOUTH));
        pws.add(getImageLabel(countLabel, imageLabel, priceLabel), getGBConstraints(2, 0, 1, 1, GridBagConstraints.CENTER));
        pws.add(typeCBox, getGBConstraints(1, 0, 1, 1, GridBagConstraints.CENTER));
        pws.add(buttonPanel, getGBConstraints(3, 0, 1, 1, GridBagConstraints.NORTH));
        return pws;
    }

    private JMenu getJMenu() {
        JMenu general = new JMenu("Menu");
        JMenuItem jMenuTrans = new JMenuItem("Transaction");

        jMenuTrans.addActionListener(e -> {
            if (windowTransaction.isVisible() == false) {
                windowTransaction = getWindowTransaction();
                f.add(windowTransaction);
                windowShop.setVisible(false);
                windowTransaction.setVisible(true);
            }
            f.repaint();
        });
        JMenuItem jMenuShop = new JMenuItem("Shop");
        jMenuShop.addActionListener(e -> {
            windowShop.setVisible(true);
            windowTransaction.setVisible(false);
            f.repaint();
        });
        JMenuItem jMenuCustomer = new JMenuItem("Customer");
        jMenuShop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowShop.setVisible(false);
                windowTransaction.setVisible(false);
                f.repaint();
            }
        });
        general.add(jMenuTrans);
        general.add(jMenuShop);
        general.add(jMenuCustomer);
        return general;
    }

    private GridBagConstraints getGBConstraints(int column, int line, int height, int width, int fill) {
        GridBagConstraints gbConst = new GridBagConstraints();
        gbConst.gridx = line;
        gbConst.gridy = column;
        gbConst.gridheight = height;
        gbConst.gridwidth = width;
        gbConst.weightx = 1.0;
        gbConst.weighty = 1.0;
        gbConst.fill = fill;
        return gbConst;
    }

    private JPanel infoAboutBuyer(JLabel name, JLabel phone, JTextField nameField, JTextField phoneField) {
        JPanel infoBuyer = new JPanel();
        infoBuyer.setLayout(new GridLayout(2, 2));
        nameField.setPreferredSize(new Dimension(120, 20));
        infoBuyer.add(name);
        infoBuyer.add(nameField);
        infoBuyer.add(phone);
        infoBuyer.add(phoneField);
        return infoBuyer;
    }

    private JPanel getImageLabel(JLabel info, JLabel imageLabel, JLabel price) {
        JPanel il = new JPanel();
        il.setLayout(new GridLayout(1, 3));
        il.add(info);
        il.add(imageLabel);
        il.add(price);
        return il;
    }

    private JComboBox getComboBox(ArrayList<AProduct> prods) throws IOException {

        JComboBox box = new JComboBox();

        box.setPreferredSize(new Dimension(100, 40));
        ArrayList<String> prodNames = new ArrayList<>();
        prods.forEach((product -> prodNames.add(product.getName())));
//        String[] birdsName = new String[]{};
        box.setModel(new DefaultComboBoxModel(prodNames.toArray()));

        images = new ImageIcon[prodNames.size()];
        Integer[] intArray = new Integer[prodNames.size()];
        for (int i = 0; i < prodNames.size(); i++) {
            intArray[i] = new Integer(i);
            images[i] = createImageIcon("bs/" + prodNames.get(i) + ".png");
            if (images[i] != null) {
                images[i].setDescription(prodNames.get(i));
            }

        }

        ActionListener actionListener = e -> {
            for (int i = 0; i < prods.size(); i++) {
                if (typeCBox.getSelectedItem().equals(prods.get(i).getName())) {
                    toBuy = prods.get(i).left();
                    priceLabel.setText("     by one: " + prods.get(i).getPrice() + " $");
                    countLabel.setText("Quantity: " + toBuy);
                    imageLabel.setIcon(images[i]);
                    ShopWindow.this.repaint();
                }

            }
        };
        box.addActionListener(actionListener);
        box.setBackground(new Color(167, 195, 183));

        return box;
    }

    protected static ImageIcon createImageIcon(String path) throws IOException {
        Image img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Didn't find ");
        }
        if (img != null) {
            return new ImageIcon(img);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void updateNumber() {
        countLabel.setText("Quantity: " + toBuy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private String getDateToDB() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = simpleDateFormat.format(cal.getTime());
        System.out.println(formatted);
        return formatted;
    }

    public String getSUM() {
        double sum = 0;
        String[][] total = trans.getBirdsInfo();
        for (int i = 0; i < total.length; i++) {
            sum += Double.parseDouble(total[i][5]);
        }
        return String.format("%.2f", sum).replace(',', '.');
    }

}
