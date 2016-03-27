package shop.gui.secondary;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CustomerFormDocListener implements DocumentListener {

    private JTextField nameField;
    private JTextField phoneField;
    private JButton toCart;
    private JButton buy;


    public CustomerFormDocListener(
            JTextField nameField, JTextField phoneField, JButton toCart, JButton buy){

        this.nameField = nameField;
        this.phoneField = phoneField;
        this.toCart = toCart;
        this.buy = buy;
    }


    @Override
    public void changedUpdate(DocumentEvent e) {
        changed();
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        changed();
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        changed();
    }

    public void changed() {
        if (!nameField.getText().isEmpty() && (phoneField.getText().length() == 12)) {
            toCart.setEnabled(true);
            buy.setEnabled(true);
        } else {
            buy.setEnabled(false);
            toCart.setEnabled(false);
        }

    }
}

