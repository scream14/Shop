package shop.gui;

import shop.gui.secondary.*;
import shop.product.Product;
import shop.service.ShopService;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


public class ServicePanel extends JPanel {


    private ShopService service;
    private ShopPanel shopPanel;

    private JTextField addCountTextField;
    private JTextField changePriceByPercentTextField;
    private JTextField countTextField;
    private JTextField iconPathTextField;
    private JTextField nameTextField;
    private JTextField newPriceTextField;
    private JTextField typeTextField;
    private JTextField priceTextField;

    private JButton addCountButton;
    private JButton addNewProdButton;
    private JButton changePriceByPercentButton;
    private JButton chooseIconButton;
    private JButton newPriceButton;

    private JComboBox<String> nameProdComboBox;
    private JComboBox<String> typeProdComboBox;

    private JLabel addNewProdLabel;
    private JLabel typeLabel;
    private JLabel nameLabel;
    private JLabel countLabel;
    private JLabel priceLabel;
    private JLabel iconLabel;
    private JLabel changeProdLabel;


    public ServicePanel(ShopService service, ShopPanel shopPanel) {
        this.service = service;
        this.shopPanel = shopPanel;
        initComponents();
    }

    private void initComponents() {
        createAllComponents();
        initComboBox();
        initModifyTextFields();
        initModifyButtons();
        initNewProductForm();
        initComponentsLocationAndSize();
        setLayout(null);
        setPreferredSize(new Dimension(600, 600));

        setVisible(true);
    }

    private void initNewProductForm() {
        ArrayList<JTextField> textFields = getNewProductTextField();
        for (JTextField newProdField : textFields) {
            newProdField.getDocument()
                    .addDocumentListener(new NewProductFieldsDocListener(textFields, addNewProdButton));
        }
        createPathChoosers();
        createNewProductButton();
    }

    private void createNewProductButton() {
        addNewProdButton.setEnabled(false);
        addNewProdButton.setText("Add new product");
        addNewProdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkNewProductForm()) {
                    service.addNewProduct(
                            typeTextField.getText(),
                            nameTextField.getText(),
                            iconPathTextField.getText(),
                            Integer.parseInt(countTextField.getText()),
                            priceTextField.getText());
                    shopPanel.updatePanel();
                    initComboBox();
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "New product added!");
                }
            }

            private boolean checkNewProductForm() {
                return checkType() && checkName() && checkIconPath() && checkCount() && checkPrice();
            }

            private boolean checkPrice() {
                if (!service.checkNewProdPrice(priceTextField.getText())) {
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "Incorrect price!");
                    return false;
                }
                return true;
            }

            private boolean checkCount() {
                if (!service.checkNewProdCount(countTextField.getText())) {
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "Incorrect count");
                    return false;
                }
                return true;
            }

            private boolean checkIconPath() {
                if (!service.isNewProdIconPathExist(iconPathTextField.getText())) {
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "Incorrect icon path or file is not exists");
                    return false;
                }
                return true;
            }

            private boolean checkName() {
                if (!service.checkNewProdName(nameTextField.getText())) {
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "Incorrect name (use alphabet, digital or space only)");
                    return false;
                } else if (service.isProductExist(typeTextField.getText(), nameTextField.getName())) {
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "<html>" + "<b>Product already exist!</b><br>" +
                                    "<b>Please, use form right!</b></html>");
                    return false;
                }
                return true;
            }

            private boolean checkType() {
                if (!service.checkNewProdType(typeTextField.getText())) {
                    JOptionPane.showMessageDialog(getServicePanel(),
                            "Incorrect type (use alphabet or space only)");
                    return false;
                }
                return true;
            }
        });
    }

    private void createPathChoosers() {
        iconPathTextField.setEnabled(false);
        iconPathTextField.setPreferredSize(iconPathTextField.getPreferredSize());
        chooseIconButton.setText("Choose icon");
        chooseIconButton.setPreferredSize(new Dimension(110, 20));
        chooseIconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new IconFilter());
                fileChooser.showOpenDialog(getServicePanel());
                try {
                    iconPathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                } catch (NullPointerException ex) {
//                    ignore
                }
            }
        });
    }

    private void initModifyButtons() {
        initAddCountButton();
        initNewPriceButton();
        initChangePriceByPercentButton();
    }

    private void initNewPriceButton() {
        newPriceButton.setEnabled(false);
        newPriceButton.setText("New price");
        newPriceButton.addActionListener(e -> {
            if (!service.isPriceCorrect(newPriceTextField.getText())) {
                JOptionPane.showMessageDialog(getServicePanel(), "Price can't be negative or zero!");
            } else {
                for (Product product : service.getProducts()) {
                    if (product.getName().equals(nameProdComboBox.getSelectedItem())
                            && product.getType().equals(typeProdComboBox.getSelectedItem())) {

                        String oldPrice = product.getPrice().toString();
                        service.newPrice(product, newPriceTextField.getText());
                        newPriceButton.setEnabled(false);
                        try {
                            newPriceTextField.getDocument().remove(0, newPriceTextField.getText().length());
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                        getServicePanel().repaint();
                        updateShopPanel(oldPrice, product.getPrice().toString());
                        break;
                    }
                }
            }

        });
    }

    private void initChangePriceByPercentButton() {
        changePriceByPercentButton.setEnabled(false);
        changePriceByPercentButton.setText("Update price by %");
        changePriceByPercentButton.addActionListener(e -> {
            for (Product product : service.getProducts()) {
                if (product.getName().equals(nameProdComboBox.getSelectedItem())
                        && product.getType().equals(typeProdComboBox.getSelectedItem())) {

                    String oldPrice = product.getPrice().toString();
                    service.updatePriceByPercent(product, changePriceByPercentTextField.getText());
                    changePriceByPercentButton.setEnabled(false);
                    try {
                        changePriceByPercentTextField.getDocument().remove(0, changePriceByPercentTextField.getText().length());
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                    getServicePanel().repaint();
                    updateShopPanel(oldPrice, product.getPrice().toString());
                    break;
                }
            }
        });
    }

    private void updateShopPanel(String oldPrice, String newPrice) {
        shopPanel.updatePanel();
        JOptionPane.showMessageDialog(getServicePanel(),
                "<html>" + "<b>Price changed</b><br>" +
                        "<b>old price: " + oldPrice + "</b>" +
                        "<br>new price: " + newPrice + "</html>");
    }

    private void initAddCountButton() {
        addCountButton.setEnabled(false);
        addCountButton.setText("Add count");
        addCountButton.addActionListener(e -> {
            try {
                Integer.parseInt(addCountTextField.getText());
                for (Product product : service.getProducts()) {
                    if (product.getName().equals(nameProdComboBox.getSelectedItem())
                            && product.getType().equals(typeProdComboBox.getSelectedItem())) {
                        if ((product.getCount() + Integer.parseInt(addCountTextField.getText())) < 0) {
                            JOptionPane.showMessageDialog(getServicePanel(), "Count in total must be more 0!");
                        } else {
                            int oldCount = product.getCount();
                            service.updateCount(product, Integer.parseInt(addCountTextField.getText()));
                            shopPanel.updatePanel();
                            addCountButton.setEnabled(false);
                            addCountTextField.getDocument().remove(0, addCountTextField.getText().length());
                            getServicePanel().repaint();
                            JOptionPane.showMessageDialog(getServicePanel(),
                                    "<html>" + "<b>Count changed</b><br>" +
                                            "<b>old count: " + oldCount + "</b>" +
                                            "<br>new count: " + product.getCount() + "</html>");
                            break;
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(getServicePanel(), "Must be whole number!");
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void initModifyTextFields() {
        newPriceTextField.getDocument()
                .addDocumentListener(new UpdateFieldsDocListener(newPriceTextField, newPriceButton));
        changePriceByPercentTextField
                .getDocument().addDocumentListener(new UpdateFieldsDocListener(changePriceByPercentTextField, changePriceByPercentButton));
        addCountTextField.getDocument()
                .addDocumentListener(new UpdateFieldsDocListener(addCountTextField, addCountButton));

        addModifyButtonsMouseListenerAndDocFilter();
    }

    private void addModifyButtonsMouseListenerAndDocFilter() {
        changePriceByPercentTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    newPriceTextField.getDocument().remove(0, newPriceTextField.getText().length());
                    newPriceButton.setEnabled(false);
                    addCountTextField.getDocument().remove(0, addCountTextField.getText().length());
                    addCountButton.setEnabled(false);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                repaint();
            }
        });
        PlainDocument doc = (PlainDocument) changePriceByPercentTextField.getDocument();
        doc.setDocumentFilter(new TextFieldFilters(FilterType.SERVICE_UPDATE_PRICE));

        newPriceTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    changePriceByPercentTextField.getDocument().remove(0, changePriceByPercentTextField.getText().length());
                    changePriceByPercentButton.setEnabled(false);
                    addCountTextField.getDocument().remove(0, addCountTextField.getText().length());
                    addCountButton.setEnabled(false);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                repaint();

            }
        });
        doc = (PlainDocument) newPriceTextField.getDocument();
        doc.setDocumentFilter(new TextFieldFilters(FilterType.SERVICE_UPDATE_PRICE));

        addCountTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    changePriceByPercentTextField.getDocument().remove(0, changePriceByPercentTextField.getText().length());
                    changePriceByPercentButton.setEnabled(false);
                    newPriceTextField.getDocument().remove(0, newPriceTextField.getText().length());
                    newPriceButton.setEnabled(false);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                repaint();
            }
        });
        doc = (PlainDocument) addCountTextField.getDocument();
        doc.setDocumentFilter(new TextFieldFilters(FilterType.SERVICE_UPDATE_COUNT));
    }

    private void initComboBox() {
        Set<String> prodTypes = new LinkedHashSet<>();
        ArrayList<String> prodNames = new ArrayList<>();
        ArrayList<Product> products = service.getProducts();

        products.stream().forEach(product -> prodTypes.add(product.getType()));

        typeProdComboBox.setModel(new DefaultComboBoxModel(prodTypes.toArray()));
        typeProdComboBox.setSelectedIndex(0);
        typeProdComboBox.addActionListener(e -> {
            prodNames.clear();
            chooseComBox(prodTypes, typeProdComboBox, products, nameProdComboBox, prodNames);
        });

        chooseComBox(prodTypes, typeProdComboBox, products, nameProdComboBox, prodNames);

        typeProdComboBox.setBackground(new Color(167, 195, 183));
        nameProdComboBox.setBackground(new Color(167, 195, 183));
    }

    private void chooseComBox(Set<String> prodTypes, JComboBox prodTypeBox,
                              ArrayList<Product> products, JComboBox prodNameBox, ArrayList<String> prodNames) {

        for (String type : prodTypes) {
            if (prodTypeBox.getSelectedItem().equals(type)) {
                for (Product product : products) {
                    if (product.getType().equals(type)) {
                        prodNames.add(product.getName());
                    }
                }
                prodNameBox.setModel(new DefaultComboBoxModel(prodNames.toArray()));
                prodNameBox.setSelectedIndex(0);
            }
        }
    }

    public ArrayList<JTextField> getNewProductTextField() {
        ArrayList<JTextField> newProductTextField = new ArrayList<>();
        newProductTextField.add(typeTextField);
        newProductTextField.add(nameTextField);
        newProductTextField.add(countTextField);
        newProductTextField.add(priceTextField);
        newProductTextField.add(iconPathTextField);
        return newProductTextField;
    }

    private JPanel getServicePanel() {
        return this;
    }

    private void createAllComponents() {
        addNewProdLabel = new JLabel("Add new product form");
        typeLabel = new JLabel("Type");
        nameLabel = new JLabel("Name");
        countLabel = new JLabel("Count");
        priceLabel = new JLabel("Price");
        iconLabel = new JLabel("Icon");
        changeProdLabel = new JLabel("Update product form");

        typeTextField = new JTextField();
        nameTextField = new JTextField();
        countTextField = new JTextField();
        priceTextField = new JTextField();
        newPriceTextField = new JTextField();
        iconPathTextField = new JTextField();
        addCountTextField = new JTextField();
        changePriceByPercentTextField = new JTextField();

        addNewProdButton = new JButton();
        chooseIconButton = new JButton();
        changePriceByPercentButton = new JButton();
        newPriceButton = new JButton();
        addCountButton = new JButton();

        typeProdComboBox = new JComboBox<>();
        nameProdComboBox = new JComboBox<>();

        add(addNewProdLabel);
        add( typeLabel);
        add(nameLabel);
        add(countLabel);
        add( priceLabel);
        add( iconLabel);
        add( changeProdLabel);

        add(typeTextField);
        add(nameTextField);
        add(countTextField);
        add(priceTextField);
        add(newPriceTextField);
        add(iconPathTextField);
        add(addCountTextField);
        add(changePriceByPercentTextField);

        add(addNewProdButton);
        add(chooseIconButton);
        add(changePriceByPercentButton);
        add(newPriceButton);
        add(addCountButton);

        add(typeProdComboBox);
        add(nameProdComboBox);

    }

    private void initComponentsLocationAndSize() {
        //new product form
        int firstColumnX = 10;
        int secondColumnX = 70;
        addNewProdLabel.setBounds(              50, 10, addNewProdLabel.getPreferredSize().width, addNewProdLabel.getPreferredSize().height);
        typeLabel.setBounds(          firstColumnX, 40, typeLabel.getPreferredSize().width, typeLabel.getPreferredSize().height);
        typeTextField.setBounds(     secondColumnX, 40, 110, 20);
        nameLabel.setBounds(          firstColumnX, 70, nameLabel.getPreferredSize().width, nameLabel.getPreferredSize().height);
        nameTextField.setBounds(     secondColumnX, 70, 110, 20);
        countLabel.setBounds(        firstColumnX, 100, countLabel.getPreferredSize().width, countLabel.getPreferredSize().height);
        countTextField.setBounds(   secondColumnX, 100, 110, 20);
        priceLabel.setBounds(        firstColumnX, 130, priceLabel.getPreferredSize().width, priceLabel.getPreferredSize().height);
        priceTextField.setBounds(   secondColumnX, 130, 110, 20);
        iconLabel.setBounds(         firstColumnX, 160, iconLabel.getPreferredSize().width, iconLabel.getPreferredSize().height);
        chooseIconButton.setBounds( secondColumnX, 160, chooseIconButton.getPreferredSize().width, chooseIconButton.getPreferredSize().height);
        iconPathTextField.setBounds( firstColumnX, 190, 170, 20);
        addNewProdButton.setBounds(  firstColumnX, 220, addNewProdButton.getPreferredSize().width, addNewProdButton.getPreferredSize().height);

        //update products form
        int thirdColumnX = 310;
        int fourthColumnX = 470;
        changeProdLabel.setBounds(                thirdColumnX, 10, changeProdLabel.getPreferredSize().width, changeProdLabel.getPreferredSize().height);
        typeProdComboBox.setBounds(               thirdColumnX, 40, 160, 20);
        nameProdComboBox.setBounds(               thirdColumnX, 70, 160, 20);
        addCountButton.setBounds(                thirdColumnX, 100, 150, 20);
        addCountTextField.setBounds(            fourthColumnX, 100, 110, 20);
        changePriceByPercentButton.setBounds(    thirdColumnX, 130, 150, 20);
        changePriceByPercentTextField.setBounds(fourthColumnX, 130, 110, 20);
        newPriceButton.setBounds(                thirdColumnX, 160, 150, 20);
        newPriceTextField.setBounds(            fourthColumnX, 160, 110, 20);
    }
}
