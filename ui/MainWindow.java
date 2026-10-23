package ui;

import models.Clothing;
import models.Electronics;
import models.Food;
import models.Product;
import utils.AppPaths;
import utils.FileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainWindow extends JFrame {
    private final Color navy = new Color(25, 39, 67);
    private final Color blue = new Color(57, 105, 220);
    private final Color background = new Color(245, 247, 251);
    private final ArrayList<Product> products = FileManager.docFile();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel pages = new JPanel(cardLayout);
    private final JLabel pageTitle = new JLabel("Tổng quan");
    private final JLabel totalProducts = new JLabel("0");
    private final JLabel totalUnits = new JLabel("0");
    private final JLabel lowStock = new JLabel("0");
    private final JLabel totalValue = new JLabel("0 đ");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Mã SP", "Tên sản phẩm", "Danh mục", "Tồn kho", "Giá bán", "Thông tin thêm"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);
    private JTextField searchField;
    private final java.util.List<String[]> salesHistory = new ArrayList<>();
    private final DefaultTableModel salesTableModel = new DefaultTableModel(
            new Object[]{"Thời gian", "Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private JTable salesTable;

    public MainWindow() {
        super("Quản lý cửa hàng");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(createAppIcon());
        setMinimumSize(new Dimension(1050, 680));
        setSize(1200, 760);
        setLocationRelativeTo(null);
        getContentPane().setBackground(background);
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(background);
        main.setBorder(new EmptyBorder(24, 28, 28, 28));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 25));
        pageTitle.setForeground(navy);
        JLabel subtitle = new JLabel("Quản lý hàng hóa và hoạt động bán hàng");
        subtitle.setForeground(new Color(115, 125, 145));
        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.add(pageTitle);
        titleStack.add(Box.createVerticalStrut(6));
        titleStack.add(subtitle);
        header.add(titleStack, BorderLayout.WEST);
        JButton addButton = new JButton("+  Thêm sản phẩm");
        addButton.setBackground(blue);
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addProduct());
        header.add(addButton, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        pages.add(buildDashboard(), "dashboard");
        pages.add(buildProductsPage(), "products");
        pages.add(buildSalesPage(), "sales");
        main.add(pages, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        loadSalesHistory();
        refreshProducts();
        refreshDashboard();
        refreshSalesHistory();
        setTitle("Quản lý cửa hàng  •  Dữ liệu: " + AppPaths.getDataDirectory());
    }


    private static Image createAppIcon() {
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(128, 128, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(12, 29, 72)); g.fillRoundRect(2, 2, 124, 124, 28, 28);
        g.setColor(new Color(23, 190, 190)); g.fillRoundRect(25, 24, 78, 12, 8, 8);
        int[] xs = {20, 108, 98, 30}; int[] ys = {43, 43, 59, 59};
        g.setColor(new Color(255, 174, 54)); g.fillPolygon(xs, ys, 4);
        g.setColor(new Color(255, 244, 220)); g.fillRoundRect(31, 57, 66, 45, 5, 5);
        g.setColor(new Color(12, 29, 72)); g.fillRoundRect(40, 68, 22, 34, 4, 4);
        g.setColor(new Color(23, 190, 190)); g.fillRoundRect(70, 68, 17, 17, 4, 4);
        g.setColor(new Color(255, 153, 35)); g.fillRoundRect(56, 83, 38, 29, 5, 5);
        g.setColor(new Color(255, 215, 117)); g.fillRect(72, 83, 6, 29);
        g.dispose(); return image;
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(230, 0));
        side.setBackground(navy);
        side.setBorder(new EmptyBorder(26, 16, 20, 16));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("  ◈  STORE DESK");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 19));
        brand.setBorder(new EmptyBorder(0, 0, 30, 0));
        side.add(brand);
        JLabel group = new JLabel("  KHÔNG GIAN LÀM VIỆC");
        group.setForeground(new Color(155, 170, 197));
        group.setFont(new Font("SansSerif", Font.BOLD, 10));
        group.setBorder(new EmptyBorder(0, 0, 12, 0));
        side.add(group);
        side.add(navButton("▦   Tổng quan", "dashboard", "Tổng quan"));
        side.add(Box.createVerticalStrut(8));
        side.add(navButton("▤   Sản phẩm", "products", "Quản lý sản phẩm"));
        side.add(Box.createVerticalStrut(8));
        side.add(navButton("＄   Bán hàng", "sales", "Bán hàng"));
        side.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("<html><div style='width:170px'>QUẢN LÝ CỬA HÀNG<br><span style='color:#9BAAC5'>Phiên bản desktop 1.0</span></div></html>");
        footer.setForeground(Color.WHITE);
        footer.setBorder(new EmptyBorder(16, 4, 0, 0));
        side.add(footer);
        return side;
    }

    private JButton navButton(String text, String page, String title) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(195, 44));
        button.setBackground(navy);
        button.setForeground(new Color(225, 232, 244));
        button.setBorder(new EmptyBorder(10, 12, 10, 10));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            pageTitle.setText(title);
            cardLayout.show(pages, page);
            if (page.equals("products")) refreshProducts();
            if (page.equals("dashboard")) refreshDashboard();
            if (page.equals("sales")) refreshSalesHistory();
        });
        return button;
    }

    private JPanel buildDashboard() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 14));
        cards.setOpaque(false);
        cards.add(statCard("MẶT HÀNG", totalProducts, "Số loại sản phẩm"));
        cards.add(statCard("TỔNG TỒN KHO", totalUnits, "Tổng số lượng"));
        cards.add(statCard("SẮP HẾT HÀNG", lowStock, "Tồn kho ≤ 5"));
        cards.add(statCard("GIÁ TRỊ TỒN", totalValue, "Theo giá bán hiện tại"));
        JPanel welcome = new JPanel(new BorderLayout(10, 12));
        welcome.setBackground(Color.WHITE);
        welcome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 234, 242)),
                new EmptyBorder(24, 24, 24, 24)));
        JLabel heading = new JLabel("Chào mừng đến với Store Desk");
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setForeground(navy);
        JLabel description = new JLabel("<html><div style='width:600px'>Theo dõi kho hàng, cập nhật thông tin sản phẩm và tạo giao dịch bán hàng trong một giao diện gọn gàng. Dữ liệu sẽ được lưu tự động trên máy tính này.</div></html>");
        description.setForeground(new Color(100, 112, 132));
        JPanel words = new JPanel();
        words.setOpaque(false);
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));
        words.add(heading);
        words.add(Box.createVerticalStrut(12));
        words.add(description);
        JButton view = new JButton("Mở danh sách sản phẩm  →");
        view.setBackground(blue);
        view.setForeground(Color.WHITE);
        view.addActionListener(e -> { pageTitle.setText("Quản lý sản phẩm"); cardLayout.show(pages, "products"); refreshProducts(); });
        welcome.add(words, BorderLayout.CENTER);
        welcome.add(view, BorderLayout.SOUTH);
        panel.add(cards, BorderLayout.NORTH);
        panel.add(welcome, BorderLayout.CENTER);
        return panel;
    }

    private JPanel statCard(String heading, JLabel value, String note) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 234, 242)),
                new EmptyBorder(18, 16, 18, 16)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(heading);
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(115, 125, 145));
        value.setFont(new Font("SansSerif", Font.BOLD, 25));
        value.setForeground(navy);
        JLabel hint = new JLabel(note);
        hint.setForeground(new Color(140, 149, 166));
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        card.add(label); card.add(Box.createVerticalStrut(12)); card.add(value);
        card.add(Box.createVerticalStrut(6)); card.add(hint);
        return card;
    }

    private JPanel buildProductsPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        JPanel tools = new JPanel(new BorderLayout(12, 0));
        tools.setOpaque(false);
        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Tìm theo mã hoặc tên sản phẩm...");
        searchField.addActionListener(e -> refreshProducts());
        JButton search = new JButton("Tìm kiếm");
        search.addActionListener(e -> refreshProducts());
        JButton reset = new JButton("Làm mới");
        reset.addActionListener(e -> { searchField.setText(""); refreshProducts(); });
        JPanel searchActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchActions.setOpaque(false);
        searchActions.add(search); searchActions.add(reset);
        tools.add(searchField, BorderLayout.CENTER);
        tools.add(searchActions, BorderLayout.EAST);
        table.setRowHeight(38);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 238, 244));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(238, 242, 249));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        JButton edit = new JButton("Sửa thông tin");
        edit.addActionListener(e -> editSelected());
        JButton delete = new JButton("Xóa sản phẩm");
        delete.addActionListener(e -> deleteSelected());
        JButton sell = new JButton("Bán sản phẩm");
        sell.setBackground(blue); sell.setForeground(Color.WHITE);
        sell.addActionListener(e -> sellSelected());
        actions.add(edit); actions.add(delete); actions.add(sell);
        panel.add(tools, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildSalesPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        JLabel heading = new JLabel("Lịch sử giao dịch");
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setForeground(navy);
        JButton sell = new JButton("+ Tạo giao dịch bán hàng");
        sell.setBackground(blue);
        sell.setForeground(Color.WHITE);
        sell.addActionListener(e -> {
            pageTitle.setText("Quản lý sản phẩm");
            cardLayout.show(pages, "products");
            refreshProducts();
            JOptionPane.showMessageDialog(this,
                    "Chọn sản phẩm trong bảng rồi bấm “Bán sản phẩm” để tạo giao dịch.",
                    "Tạo giao dịch", JOptionPane.INFORMATION_MESSAGE);
        });
        toolbar.add(heading, BorderLayout.WEST);
        toolbar.add(sell, BorderLayout.EAST);

        salesTable = new JTable(salesTableModel);
        salesTable.setRowHeight(36);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salesTable.setShowVerticalLines(false);
        salesTable.setGridColor(new Color(235, 238, 244));
        salesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        salesTable.getTableHeader().setBackground(new Color(238, 242, 249));
        JScrollPane scroll = new JScrollPane(salesTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)));

        JLabel note = new JLabel("Các giao dịch bán hàng được lưu trên máy này.");
        note.setForeground(new Color(115, 125, 145));
        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(note, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshSalesHistory() {
        salesTableModel.setRowCount(0);
        for (int i = salesHistory.size() - 1; i >= 0; i--) {
            String[] sale = salesHistory.get(i);
            salesTableModel.addRow(new Object[]{sale[0], sale[1], sale[2], sale[3],
                    money(Long.parseLong(sale[4])), money(Long.parseLong(sale[5]))});
        }
    }

    private Path getSalesHistoryFile() throws IOException {
        return AppPaths.getDataDirectory().resolve("sales_history.tsv");
    }

    private void loadSalesHistory() {
        try {
            Path file = getSalesHistoryFile();
            if (!Files.exists(file)) return;
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                String[] parts = line.replace("\\t", "\t").split("\\t", -1);
                if (parts.length != 6) continue;
                try {
                    String name = new String(Base64.getDecoder().decode(parts[2]), StandardCharsets.UTF_8);
                    salesHistory.add(new String[]{parts[0], parts[1], name, parts[3], parts[4], parts[5]});
                } catch (IllegalArgumentException ignored) { }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Không đọc được lịch sử bán hàng: " + ex.getMessage(),
                    "Lỗi dữ liệu", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void appendSale(Product product, int quantity, long unitPrice, long amount) throws IOException {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String encodedName = Base64.getEncoder().encodeToString(product.getName().getBytes(StandardCharsets.UTF_8));
        String[] sale = {time, product.getId(), product.getName(), Integer.toString(quantity),
                Long.toString(unitPrice), Long.toString(amount)};
        String line = String.join("\t", time, product.getId(), encodedName,
                Integer.toString(quantity), Long.toString(unitPrice), Long.toString(amount)) + System.lineSeparator();
        Files.writeString(getSalesHistoryFile(), line, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        salesHistory.add(sale);
    }

    private void refreshProducts() {
        tableModel.setRowCount(0);
        String query = searchField == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        for (Product p : products) {
            if (!query.isEmpty() && !p.getId().toLowerCase(Locale.ROOT).contains(query)
                    && !p.getName().toLowerCase(Locale.ROOT).contains(query)) continue;
            tableModel.addRow(new Object[]{p.getId(), p.getName(), typeName(p), p.getQuantity(),
                    money((long) p.calculateFinalPrice()), extraInfo(p)});
        }
    }

    private void refreshDashboard() {
        totalProducts.setText(Integer.toString(products.size()));
        int units = 0, low = 0;
        long value = 0;
        for (Product p : products) {
            units += p.getQuantity();
            if (p.getQuantity() <= 5) low++;
            value += (long) p.calculateFinalPrice() * p.getQuantity();
        }
        totalUnits.setText(Integer.toString(units));
        lowStock.setText(Integer.toString(low));
        totalValue.setText(money(value));
    }

    private void addProduct() {
        ProductDialog.Result result = ProductDialog.show(this, null);
        if (result == null) return;
        if (findById(result.product().getId()) != null) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại.", "Không thể thêm", JOptionPane.WARNING_MESSAGE);
            return;
        }
        products.add(result.product());
        saveAndRefresh();
    }

    private void editSelected() {
        Product old = selectedProduct();
        if (old == null) return;
        ProductDialog.Result result = ProductDialog.show(this, old);
        if (result == null) return;
        Product replacement = result.product();
        replacement.setId(old.getId());
        int index = products.indexOf(old);
        products.set(index, replacement);
        saveAndRefresh();
    }

    private void deleteSelected() {
        Product product = selectedProduct();
        if (product == null) return;
        int choice = JOptionPane.showConfirmDialog(this,
                "Xóa sản phẩm “" + product.getName() + "” khỏi kho?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            products.remove(product);
            saveAndRefresh();
        }
    }

    private void sellSelected() {
        Product product = selectedProduct();
        if (product == null) return;
        if (product.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng.", "Không thể bán", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String value = JOptionPane.showInputDialog(this,
                "Sản phẩm: " + product.getName() + "\nTồn kho: " + product.getQuantity()
                        + "\nĐơn giá: " + money((long) product.calculateFinalPrice())
                        + "\nNhập số lượng khách mua:", "Bán hàng", JOptionPane.QUESTION_MESSAGE);
        if (value == null) return;
        try {
            int quantity = Integer.parseInt(value.trim());
            if (quantity <= 0 || quantity > product.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0 và không vượt quá tồn kho.");
                return;
            }
            long unitPrice = (long) product.calculateFinalPrice();
            long amount = unitPrice * quantity;
            appendSale(product, quantity, unitPrice, amount);
            product.setQuantity(product.getQuantity() - quantity);
            saveAndRefresh();
            refreshSalesHistory();
            JOptionPane.showMessageDialog(this,
                    "BÁN HÀNG THÀNH CÔNG\n\n" + product.getName() + " × " + quantity
                            + "\nTổng thanh toán: " + money(amount),
                    "Hóa đơn bán hàng", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên hợp lệ.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Không thể lưu giao dịch: " + ex.getMessage(), "Lỗi lưu dữ liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Product selectedProduct() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sản phẩm trong bảng trước.");
            return null;
        }
        String id = tableModel.getValueAt(table.convertRowIndexToModel(row), 0).toString();
        return findById(id);
    }

    private Product findById(String id) {
        for (Product p : products) if (p.getId().equalsIgnoreCase(id)) return p;
        return null;
    }

    private void saveAndRefresh() {
        FileManager.ghiFile(products);
        refreshProducts();
        refreshDashboard();
    }

    private static String typeName(Product p) {
        if (p instanceof Food) return "Thực phẩm";
        if (p instanceof Electronics) return "Điện tử";
        if (p instanceof Clothing) return "Quần áo";
        return "Khác";
    }

    private static String extraInfo(Product p) {
        if (p instanceof Food) return "Hạn dùng: " + ((Food) p).getExpiryDays() + " ngày";
        if (p instanceof Electronics) return "Bảo hành: " + ((Electronics) p).getWarrantyMonths() + " tháng";
        if (p instanceof Clothing) return "Size: " + ((Clothing) p).getSize();
        return "";
    }

    private static String money(long value) {
        return NumberFormat.getIntegerInstance(new Locale("vi", "VN")).format(value) + " đ";
    }
}
