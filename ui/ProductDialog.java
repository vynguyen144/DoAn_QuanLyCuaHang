package ui;

import models.Clothing;
import models.Electronics;
import models.Food;
import models.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

final class ProductDialog {
    record Result(Product product) {}

    private ProductDialog() {}

    static Result show(Component parent, Product existing) {
        JComboBox<String> type = new JComboBox<>(new String[]{"Thực phẩm", "Đồ điện tử", "Quần áo"});
        JTextField id = new JTextField(existing == null ? "" : existing.getId(), 20);
        JTextField name = new JTextField(existing == null ? "" : existing.getName(), 20);
        JSpinner quantity = new JSpinner(new SpinnerNumberModel(existing == null ? 0 : existing.getQuantity(), 0, 1_000_000, 1));
        JSpinner price = new JSpinner(new SpinnerNumberModel(existing == null ? 0 : existing.getBasePrice(), 0, Integer.MAX_VALUE, 1000));
        JTextField extraText = new JTextField(20);
        JSpinner extraNumber = new JSpinner(new SpinnerNumberModel(0, 0, 100_000, 1));
        JLabel extraLabel = new JLabel("Thông tin thêm");
        boolean editing = existing != null;
        if (existing instanceof Food) {
            type.setSelectedIndex(0); extraNumber.setValue(((Food) existing).getExpiryDays());
        } else if (existing instanceof Electronics) {
            type.setSelectedIndex(1); extraNumber.setValue(((Electronics) existing).getWarrantyMonths());
        } else if (existing instanceof Clothing) {
            type.setSelectedIndex(2); extraText.setText(((Clothing) existing).getSize());
        }
        id.setEnabled(!editing);
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBorder(new EmptyBorder(10, 4, 4, 4));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 5, 7, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        addRow(fields, c, 0, "Loại sản phẩm", type);
        addRow(fields, c, 1, "Mã sản phẩm", id);
        addRow(fields, c, 2, "Tên sản phẩm", name);
        addRow(fields, c, 3, "Số lượng tồn", quantity);
        addRow(fields, c, 4, "Giá gốc (VNĐ)", price);
        JPanel extraHolder = new JPanel(new BorderLayout());
        extraHolder.add(extraNumber, BorderLayout.CENTER);
        updateExtra(type, extraLabel, extraNumber, extraText);
        JPanel dynamic = new JPanel(new BorderLayout(8, 0));
        dynamic.add(extraNumber, BorderLayout.CENTER);
        addRow(fields, c, 5, extraLabel.getText(), dynamic);
        type.addActionListener(e -> {
            updateExtra(type, extraLabel, extraNumber, extraText);
            dynamic.removeAll();
            dynamic.add(type.getSelectedIndex() == 2 ? extraText : extraNumber, BorderLayout.CENTER);
            fields.getComponent(5); // Keep the row in place; update its label below.
            Component[] row = fields.getComponents();
            for (Component comp : row) {
                if (comp instanceof JLabel label && label.getText().equals("Thông tin thêm")) label.setText(extraLabel.getText());
            }
            dynamic.revalidate(); dynamic.repaint(); fields.revalidate();
        });
        // Ensure the right editor is displayed for the initial product type.
        dynamic.removeAll();
        dynamic.add(type.getSelectedIndex() == 2 ? extraText : extraNumber, BorderLayout.CENTER);
        int choice = JOptionPane.showConfirmDialog(parent, fields,
                editing ? "Chỉnh sửa sản phẩm" : "Thêm sản phẩm mới",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return null;
        String productId = id.getText().trim(), productName = name.getText().trim();
        if (productId.isEmpty() || productName.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Mã và tên sản phẩm không được để trống.");
            return null;
        }
        Product product;
        switch (type.getSelectedIndex()) {
            case 0 -> { Food p = new Food(); p.setExpiryDays((Integer) extraNumber.getValue()); product = p; }
            case 1 -> { Electronics p = new Electronics(); p.setWarrantyMonths((Integer) extraNumber.getValue()); product = p; }
            default -> { Clothing p = new Clothing(); p.setSize(extraText.getText().trim()); product = p; }
        }
        product.setId(productId);
        product.setName(productName);
        product.setQuantity((Integer) quantity.getValue());
        product.setBasePrice((Integer) price.getValue());
        return new Result(product);
    }

    private static void updateExtra(JComboBox<String> type, JLabel label, JSpinner number, JTextField text) {
        switch (type.getSelectedIndex()) {
            case 0 -> label.setText("Số ngày còn hạn");
            case 1 -> label.setText("Bảo hành (tháng)");
            default -> label.setText("Kích cỡ (S/M/L/XL)");
        }
    }

    private static void addRow(JPanel panel, GridBagConstraints c, int row, String label, Component input) {
        c.gridy = row; c.gridx = 0; c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(input, c);
    }
}
