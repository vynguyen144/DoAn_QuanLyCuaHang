package ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class DesktopApp {
    private DesktopApp() {}

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Component.arc", 12);
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Table.rowHeight", 34);
        } catch (Exception ex) {
            System.err.println("Không thể bật giao diện FlatLaf: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
