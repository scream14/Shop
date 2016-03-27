package shop.gui;

import shop.gui.secondary.Cart;
import shop.gui.secondary.FilterType;
import shop.gui.secondary.CustomerFormDocListener;
import shop.gui.secondary.TextFieldFilters;
import shop.product.Product;
import shop.service.*;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


public class ShopPanel extends JPanel {


    private JLabel countLabel;
    private JLabel imageLabel;
    private JLabel priceLabel;
    private JLabel customerNameLabel;
    private JLabel customerPhoneNumberLabel;

    private JTextField nameCustomerField;
    private JTextField phoneCustomerField;

    private JComboBox typeOfProductsBox;
    private JComboBox nameOfProductsBox;

    private JButton addCountToBuyButton;
    private JButton removeCountToBuyButton;
    private JButton addTenCountToBuyButton;
    private JButton removeTenCountToBuyButton;
    private JButton putToCartButton;
    private JButton openCartButton;
    private JButton buyButton;
    private ArrayList<JButton> allButtons;

    private int productCount;
    private int toBuy;
    private Cart cart;
    private ArrayList<Product> productInCart;

    private ShopService service;


    public ShopPanel(ShopService service) {

        this.service = service;
        productInCart = new ArrayList<>();
        allButtons = new ArrayList<>();

        typeOfProductsBox = new JComboBox();
        nameOfProductsBox = new JComboBox();

        add(typeOfProductsBox);
        add(nameOfProductsBox);

        makeButton();
        initProductsChooser();
        initCustomerForm();
        initSizeAndLocationOfAllComponents();

        setLayout(null);
        setPreferredSize(new Dimension(600, 600));

        setVisible(true);
    }

    private void initCustomerForm() {
        customerNameLabel =          new JLabel("Name");
        customerPhoneNumberLabel =   new JLabel("Phone (12 digits)");
        nameCustomerField =     new JTextField();
        phoneCustomerField =    new JTextField();

        phoneCustomerField.setText("380");

        nameCustomerField.getDocument().addDocumentListener(
                new CustomerFormDocListener(nameCustomerField, phoneCustomerField, putToCartButton, buyButton));
        PlainDocument doc = (PlainDocument) nameCustomerField.getDocument();
        doc.setDocumentFilter(new TextFieldFilters(FilterType.CUSTOMER_NAME));

        phoneCustomerField.getDocument().addDocumentListener(
                new CustomerFormDocListener(nameCustomerField, phoneCustomerField, putToCartButton, buyButton));
        doc = (PlainDocument) phoneCustomerField.getDocument();
        doc.setDocumentFilter(new TextFieldFilters(FilterType.CUSTOMER_PHONE));

        add(customerNameLabel);
        add(nameCustomerField);
        add(customerPhoneNumberLabel);
        add(phoneCustomerField);
    }

    private void makeButton() {
        buyButton =                 new JButton("Buy");
        putToCartButton =           new JButton("Add to Cart");
        openCartButton =            new JButton("Cart");
        addCountToBuyButton =       new JButton(">");
        removeCountToBuyButton =    new JButton("<");
        addTenCountToBuyButton =    new JButton(">>");
        removeTenCountToBuyButton = new JButton("<<");

        add(buyButton);
        add(openCartButton);
        add(putToCartButton);
        add(addCountToBuyButton);
        add(removeCountToBuyButton);
        add(addTenCountToBuyButton);
        add(removeTenCountToBuyButton);

        allButtons.add(buyButton);
        allButtons.add(putToCartButton);
        allButtons.add(addCountToBuyButton);
        allButtons.add(addTenCountToBuyButton);
        allButtons.add(removeCountToBuyButton);
        allButtons.add(removeTenCountToBuyButton);

        addActionListenersForEachButton();

        buyButton.setBackground(new Color(0, 255, 53));
        buyButton.setEnabled(false);
        putToCartButton.setEnabled(false);
    }
    public void initProductsChooser() {
        Set<String> prodTypes = new LinkedHashSet<>();
        ArrayList<String> prodNames = new ArrayList<>();

        service.getProducts().stream().forEach(product -> prodTypes.add(product.getType()));

        typeOfProductsBox.setModel(new DefaultComboBoxModel(prodTypes.toArray()));
        typeOfProductsBox.setSelectedIndex(0);
        typeOfProductsBox.addActionListener(e -> {
            prodNames.clear();
            chooseComBox(prodTypes, typeOfProductsBox, nameOfProductsBox, prodNames);
        });

        chooseComBox(prodTypes, typeOfProductsBox, nameOfProductsBox, prodNames);

        nameOfProductsBox.addActionListener(e -> {
            updateLabels();
            checkProdCounter();
        });

        initImageLabelPanel(service.getProducts().get(0));

        typeOfProductsBox.setBackground(new Color(167, 195, 183));
        nameOfProductsBox.setBackground(new Color(167, 195, 183));
    }

    private void initImageLabelPanel(Product product) {
        productCount = product.getCount();
        toBuy = productCount;

        countLabel = new JLabel("" + productCount);
        imageLabel = new JLabel(product.getImage());
        priceLabel = new JLabel("" + product.getPrice() + " $");

        add(countLabel);
        add(imageLabel);
        add(priceLabel);
    }

    private void addActionListenersForEachButton() {
        addCountToBuyButton.addActionListener(e -> {
            if (toBuy < productCount) {
                ++toBuy;
            }
            updateCounter();
        });
        addTenCountToBuyButton.addActionListener(e -> {
            if (toBuy < productCount) {
                toBuy += 10;
            }
            if (toBuy > productCount) {
                toBuy = productCount;
            }
            updateCounter();
        });
        removeCountToBuyButton.addActionListener(e -> {
            if (toBuy > 0) {
                --toBuy;
            }
            if (toBuy <= 0) {
                toBuy = 1;
            }
            updateCounter();
        });
        removeTenCountToBuyButton.addActionListener(e -> {
            if (toBuy > 0) {
                toBuy -= 10;
            }
            if (toBuy <= 0) {
                toBuy = 1;
            }
            updateCounter();
        });
        openCartButton.addActionListener(e -> {
            if (!productInCart.isEmpty()) {
                cart = new Cart(productInCart);
                cart.makeVisible();
            } else {
                JOptionPane.showMessageDialog(this, "Cart is empty");
            }
        });
        buyButton.addActionListener(e -> {
            if (checkCustomerFormTextField()) {
                if (productInCart.isEmpty()) {
                    putToCartSelectedProduct();
                    buySelected();
                } else {
                    productInCart = cart.getProducts();
                    buySelected();
                }
            }
        });
        putToCartButton.addActionListener(e -> putToCartSelectedProduct());
    }

    private boolean checkCustomerFormTextField() {
        if (nameCustomerField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Please, write your name");
            return false;
        } else if (phoneCustomerField.getText().length() != 12) {
            JOptionPane.showMessageDialog(this, "Incorrect phone: must be 12 digits");
            return false;
        }
        return true;
    }

    private void buySelected() {
        String report =
                service.buyProducts(new Customer(nameCustomerField.getText(), phoneCustomerField.getText()), productInCart);
        JOptionPane.showMessageDialog(this, report);
        productInCart.clear();
    }

    private void putToCartSelectedProduct() {
        for (Product product : service.getProducts()) {
            if (nameOfProductsBox.getSelectedItem().equals(product.getName())) {
                Product copy = new Product(product);
                copy.setCount(toBuy);

                if (!isProductInCart(copy)) {
                    productInCart.add(copy);
                }

                product.addCount(-toBuy);
                toBuy = product.getCount();
                productCount = product.getCount();

                checkProdCounter();
                updateCounter();
            }
        }
    }

    private boolean isProductInCart(Product product) {
        for (Product prodFromCart : productInCart){
            if (prodFromCart.getID() == product.getID()){
                prodFromCart.addCount(product.getCount());
                return true;
            }
        }
        return false;
    }

    private void checkProdCounter() {
        if (productCount <= 0) {
            allButtons.forEach(button -> button.setEnabled(false));
        }
    }

    private void chooseComBox(Set<String> productsTypes, JComboBox productsTypeBox,
                              JComboBox productNameBox, ArrayList<String> productNames) {

        for (String type : productsTypes) {
            if (productsTypeBox.getSelectedItem().equals(type)) {
                for (Product product : service.getProducts()) {
                    if (product.getType().equals(type)) {
                        productNames.add(product.getName());
                    }
                }
                productNameBox.setModel(new DefaultComboBoxModel(productNames.toArray()));
                productNameBox.setSelectedIndex(0);
            }
        }
    }

    private void updateCounter() {
        countLabel.setText("" + toBuy);
        repaint();
    }

    private void updateLabels() {
        for (Product product : service.getProducts()) {
            if (nameOfProductsBox.getSelectedItem().equals(product.getName())) {
                productCount = product.getCount();
                toBuy = productCount;

                priceLabel.setText("" + product.getPrice() + " $");
                countLabel.setText("" + product.getCount());
                imageLabel.setIcon(product.getImage());

                repaint();
                break;
            }
        }
    }

    public void updatePanel() {
        this.repaint();
    }

    private void initSizeAndLocationOfAllComponents() {
        // choosers boxes
        typeOfProductsBox.setBounds(400, 200, 140, 40);
        nameOfProductsBox.setBounds(400, 300, 140, 40);

        // customer form components
        nameCustomerField.setPreferredSize(new Dimension(100, 20));
        phoneCustomerField.setPreferredSize(new Dimension(100, 20));

        customerNameLabel.setBounds(
                210, 30, customerNameLabel.getPreferredSize().width, customerNameLabel.getPreferredSize().height);
        customerPhoneNumberLabel.setBounds(
                210, 50, customerPhoneNumberLabel.getPreferredSize().width, customerPhoneNumberLabel.getPreferredSize().height);
        nameCustomerField.setBounds(
                310, 30, nameCustomerField.getPreferredSize().width, nameCustomerField.getPreferredSize().height);
        phoneCustomerField.setBounds(
                310, 50, phoneCustomerField.getPreferredSize().width, phoneCustomerField.getPreferredSize().height);

        // labels
        countLabel.setBounds(164, 350, countLabel.getPreferredSize().width * 10, priceLabel.getPreferredSize().height);
        imageLabel.setBounds(122, 200, imageLabel.getPreferredSize().width, imageLabel.getPreferredSize().height);
        priceLabel.setBounds(154, 165, priceLabel.getPreferredSize().width * 10, priceLabel.getPreferredSize().height);

        // buying form components
        buyButton.setBounds(                92, 382, 162, buyButton.getPreferredSize().height);
        putToCartButton.setBounds(          92, 419, 162, putToCartButton.getPreferredSize().height);
        openCartButton.setBounds(           92, 456, 162, openCartButton.getPreferredSize().height);
        addCountToBuyButton.setBounds(      207, 345, 48, addCountToBuyButton.getPreferredSize().height);
        addTenCountToBuyButton.setBounds(   254, 345, 48, addTenCountToBuyButton.getPreferredSize().height);
        removeCountToBuyButton.setBounds(   92, 345, 48, removeCountToBuyButton.getPreferredSize().height);
        removeTenCountToBuyButton.setBounds(44, 345, 48, removeTenCountToBuyButton.getPreferredSize().height);
    }
}
