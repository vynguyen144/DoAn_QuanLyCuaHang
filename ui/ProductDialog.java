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
        boolean editing = existing != null;

        if (existing instanceof Food) {
            type.setSelectedIndex(0);
            extraNumber.setValue(((Food) existing).getExpiryDays());
        } else if (existing instanceof Electronics) {
            type.setSelectedIndex(1);
            extraNumber.setValue(((Electronics) existing).getWarrantyMonths());
        } else if (existing instanceof Clothing) {
            type.setSelectedIndex(2);
            extraText.setText(((Clothing) existing).getSize());
        }
        id.setEnabled(!editing);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBorder(new EmptyBorder(10, 4, 4, 4));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 5, 7, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        addRow(fields, c, 0, new JLabel("Loại sản phẩm"), type);
        addRow(fields, c, 1, new JLabel("Mã sản phẩm"), id);
        addRow(fields, c, 2, new JLabel("Tên sản phẩm"), name);
        addRow(fields, c, 3, new JLabel("Số lượng tồn"), quantity);
        addRow(fields, c, 4, new JLabel("Giá gốc (VNĐ)"), price);

        JLabel extraLabel = new JLabel(labelFor(type.getSelectedIndex()));
        JPanel dynamic = new JPanel(new BorderLayout());
        dynamic.add(type.getSelectedIndex() == 2 ? extraText : extraNumber, BorderLayout.CENTER);
        addRow(fields, c, 5, extraLabel, dynamic);
        type.addActionListener(e -> {
            extraLabel.setText(labelFor(type.getSelectedIndex()));
            dynamic.removeAll();
            dynamic.add(type.getSelectedIndex() == 2 ? extraText : extraNumber, BorderLayout.CENTER);
            dynamic.revalidate();
            dynamic.repaint();
        });

        int choice = JOptionPane.showConfirmDialog(parent, fields,
                editing ? "Chỉnh sửa sản phẩm" : "Thêm sản phẩm mới",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return null;

        String productId = id.getText().trim();
        String productName = name.getText().trim();
        if (productId.isEmpty() || productName.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Mã và tên sản phẩm không được để trống.");
            return null;
        }

        Product product;
        switch (type.getSelectedIndex()) {
            case 0 -> {
                Food p = new Food();
                p.setExpiryDays((Integer) extraNumber.getValue());
                product = p;
            }
            case 1 -> {
                Electronics p = new Electronics();
                p.setWarrantyMonths((Integer) extraNumber.getValue());
                product = p;
            }
            default -> {
                Clothing p = new Clothing();
                p.setSize(extraText.getText().trim());
                product = p;
            }
        }
        product.setId(productId);
        product.setName(productName);
        product.setQuantity((Integer) quantity.getValue());
        product.setBasePrice((Integer) price.getValue());
        return new Result(product);
    }

    private static String labelFor(int selectedIndex) {
        return switch (selectedIndex) {
            case 0 -> "Số ngày còn hạn";
            case 1 -> "Bảo hành (tháng)";
            default -> "Kích cỡ (S/M/L/XL)";
        };
    }

    private static void addRow(JPanel panel, GridBagConstraints c, int row, JLabel label, Component input) {
        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(label, c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(input, c);
    }
}
