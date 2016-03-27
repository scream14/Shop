package shop.gui.secondary;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;


public class NewProductFieldsDocListener implements DocumentListener {


    private ArrayList<JTextField> textFields;
    private JButton button;

    public NewProductFieldsDocListener(ArrayList<JTextField> textFields, JButton button) {

        this.textFields = textFields;
        this.button = button;
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

        boolean[] flags = new boolean[textFields.size()];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = !textFields.get(i).getText().isEmpty();
        }

        boolean enabled = flags[0];
        for (boolean flag : flags) {
            enabled = enabled && flag;
        }
        button.setEnabled(enabled);

    }

}

