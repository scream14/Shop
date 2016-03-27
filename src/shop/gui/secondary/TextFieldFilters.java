package shop.gui.secondary;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;


public class TextFieldFilters extends DocumentFilter {

    FilterType filterType;

    public TextFieldFilters(FilterType filterType) {
        this.filterType = filterType;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    private boolean test(String text) {
        if (filterType == FilterType.CUSTOMER_NAME) {
            return nameCustomerTest(text);
        } else if (filterType == FilterType.CUSTOMER_PHONE) {
            return phoneCustomerTest(text);
        } else if (filterType == FilterType.SERVICE_UPDATE_COUNT) {
            return updateCountServiceTest(text);
        } else if (filterType == FilterType.SERVICE_NEW_PRICE) {
            return newPriceServiceTest(text);
        } else if (filterType == FilterType.SERVICE_UPDATE_PRICE) {
            return updatePriceServiceTest(text);
        } else return false;
    }

    private boolean nameCustomerTest(String text) {
        int maxNameLength = 20;
        int spaceCounter = 3;
        for (char ch : text.toCharArray()) {
            if (ch == text.toCharArray()[0] && Character.isSpaceChar(ch)) {
                return false;
            }
            if (!Character.isLetter(ch)) {
                if (!Character.isSpaceChar(ch)) {
                    return false;
                } else if (spaceCounter == 0) {
                    return false;
                } else {
                    spaceCounter--;
                }
            }
        }
        return text.toCharArray().length <= maxNameLength;
    }

    private boolean phoneCustomerTest(String text) {
        int maxLength = 12;
        for (char ch : text.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return text.toCharArray().length <= maxLength;
    }

    private boolean updateCountServiceTest(String text) {
        if (text.length() > 5) {
            return false;
        }
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean newPriceServiceTest(String text) {
        if (text.length() > 8) {
            return false;
        }
        try {
            double price = Double.parseDouble(text);
            return price <= 0;
        } catch (NumberFormatException e1) {
            return false;
        }
    }

    private boolean updatePriceServiceTest(String text) {
        if (text.length() > 5) {
            return false;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e1) {
            return false;
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);
        super.remove(fb, offset, length);
    }
}
