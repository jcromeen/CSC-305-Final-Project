import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuBuilder {

    /**
     * Creates a JMenu containing the specified menu items.
     *
     * @param title     The title of the menu.
     * @param menuItems The menu items to add to the menu.
     * @return A JMenu populated with the specified menu items.
     */
    public static JMenu createMenu(String title, JMenuItem... menuItems) {
        JMenu menu = new JMenu(title);
        for (JMenuItem item : menuItems) {
            menu.add(item);
        }
        return menu;
    }

    public static JMenuItem createMenuItem(String title, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    public static JCheckBoxMenuItem createCheckBoxMenuItem(String title, boolean isSelected, ActionListener listener) {
        JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(title, isSelected);
        checkBoxMenuItem.addActionListener(listener);
        return checkBoxMenuItem;
    }
}
