package shop.gui.secondary;


import java.io.File;
import javax.swing.filechooser.*;

public class IconFilter extends FileFilter {

    public final static String png = "png";

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String extension = getExtension(file);
        if (extension != null) {
            if (extension.equals(png)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public String getDescription() {
        return "icon (.png)";
    }

}
