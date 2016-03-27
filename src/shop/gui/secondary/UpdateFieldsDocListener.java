package shop.gui.secondary;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UpdateFieldsDocListener implements DocumentListener {

    private JTextField textField;
    private JButton button;

    public UpdateFieldsDocListener(JTextField textField, JButton button){

        this.textField = textField;
        this.button = button;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changed();
    }
    @Override
    public void removeUpdate(DocumentEvent e) {changed();
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        changed();
    }

    public void changed() {
            if (!textField.getText().isEmpty()) {
                button.setEnabled(true);
            } else {
                button.setEnabled(false);
            }

    }
}