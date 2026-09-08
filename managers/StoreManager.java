package managers;

import java.util.ArrayList;
import java.util.Scanner;
import models.Clothing;
import models.Electronics;
import models.Food;
import models.Product;
import transactions.Invoice;
import transactions.OrderItem;
import utils.FileManager;
import utils.InputHelper;

public class StoreManager {
    private ArrayList<Product> productList = new ArrayList<>();

    public StoreManager() {
        // Tự động đọc dữ liệu từ tệp khi khởi động ứng dụng
        this.productList = FileManager.docFile();
    }

    public void themSanPham(Scanner scanner) {
        System.out.println("\n--- THÊM SẢN PHẨM MỚI ---");
        System.out.println("1. Thực phẩm (Food)");
        System.out.println("2. Đồ điện tử (Electronics)");
        System.out.println("3. Quần áo (Clothing)");
        System.out.println("4. QUAY LẠI MENU CHÍNH");

        int loai = InputHelper.nhapSo(scanner, "Chọn loại sản phẩm (1-4)");

        if (loai == 4) {
            System.out.println("-> Đã hủy thêm sản phẩm. Đang quay về Menu chính...");
            return;
        }

        // Kiểm tra mã trùng ngay từ đầu trước khi cho phép nhập tiếp
        String maSP = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm (ID)");
        
        for (Product p : productList) {
            if (p.getId().equalsIgnoreCase(maSP)) {
                System.out.println("-> LỖI: Mã sản phẩm '" + maSP + "' đã tồn tại trong kho! Vui lòng dùng mã khác.");
                return;
            }
        }

        Product product = null;
        switch (loai) {
            case 1:
                product = new Food();
                break;
            case 2:
                product = new Electronics();
                break;
            case 3:
                product = new Clothing();
                break;
            default:
                System.out.println("-> LỖI: Lựa chọn không hợp lệ!");
                return;
        }

        // Gán mã và nhập các thông số cơ bản
        product.setId(maSP);
        System.out.println("--- Nhập thông tin chi tiết ---");
        product.setName(InputHelper.nhapChuoi(scanner, "Nhập tên sản phẩm"));
        product.setQuantity(InputHelper.nhapSo(scanner, "Nhập số lượng tồn kho"));
        product.setBasePrice(InputHelper.nhapSo(scanner, "Nhập giá gốc (VNĐ)"));
        
        // Nhập thuộc tính đặc thù riêng cho từng loại con
        if (product instanceof Food) {
            ((Food) product).setExpiryDays(InputHelper.nhapSo(scanner, "Nhập số ngày còn hạn sử dụng"));
        } else if (product instanceof Electronics) {
            ((Electronics) product).setWarrantyMonths(InputHelper.nhapSo(scanner, "Nhập thời gian bảo hành (tháng)"));
        } else if (product instanceof Clothing) {
            ((Clothing) product).setSize(InputHelper.nhapChuoi(scanner, "Nhập kích cỡ (Size S/M/L/XL)"));
        }

        productList.add(product);
        FileManager.ghiFile(productList);
        System.out.println("-> Thêm sản phẩm thành công và đã lưu vào tệp!");
    }

    public void hienThiDanhSach() {
        System.out.println("\n--- DANH SÁCH SẢN PHẨM TRONG KHO ---");
        if (productList.isEmpty()) {
            System.out.println("Kho hàng đang trống!");
            return;
        }
        for (Product p : productList) {
            p.xuatThongTin();
        }
    }

    public void suaSanPham(Scanner scanner) {
        System.out.println("\n--- CHỈNH SỬA THÔNG TIN SẢN PHẨM ---");
        String maSP = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm cần sửa");

        Product productToEdit = null;
        for (Product p : productList) {
            if (p.getId().equalsIgnoreCase(maSP)) {
                productToEdit = p;
                break;
            }
        }

        if (productToEdit == null) {
            System.out.println("-> LỖI: Không tìm thấy sản phẩm có mã '" + maSP + "' trong kho!");
            return;
        }

        System.out.println("-> Đã tìm thấy sản phẩm! Vui lòng nhập thông tin mới:");
        // Giữ lại mã cũ, chỉ cho nhập lại thông tin chi tiết
        productToEdit.setName(InputHelper.nhapChuoi(scanner, "Nhập tên sản phẩm mới"));
        productToEdit.setQuantity(InputHelper.nhapSo(scanner, "Nhập số lượng tồn kho mới"));
        productToEdit.setBasePrice(InputHelper.nhapSo(scanner, "Nhập giá gốc mới (VNĐ)"));

        if (productToEdit instanceof Food) {
            ((Food) productToEdit).setExpiryDays(InputHelper.nhapSo(scanner, "Nhập số ngày còn hạn mới"));
        } else if (productToEdit instanceof Electronics) {
            ((Electronics) productToEdit).setWarrantyMonths(InputHelper.nhapSo(scanner, "Nhập thời gian bảo hành mới (tháng)"));
        } else if (productToEdit instanceof Clothing) {
            ((Clothing) productToEdit).setSize(InputHelper.nhapChuoi(scanner, "Nhập kích cỡ mới (Size S/M/L/XL)"));
        }
        
        FileManager.ghiFile(productList);
        System.out.println("-> CẬP NHẬT THÀNH CÔNG VÀ ĐÃ LƯU VÀO TỆP!");
    }

    public void giamSoLuongSanPham(Scanner scanner) {
        System.out.println("\n--- GIẢM SỐ LƯỢNG TỒN KHO ---");
        String maSP = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm cần giảm số lượng");

        Product productToUpdate = null;
        for (Product p : productList) {
            if (p.getId().equalsIgnoreCase(maSP)) {
                productToUpdate = p;
                break;
            }
        }

        if (productToUpdate == null) {
            System.out.println("-> LỖI: Không tìm thấy sản phẩm có mã '" + maSP + "' trong kho!");
            return;
        }

        System.out.println("-> Đã tìm thấy: " + productToUpdate.getName() + " | Tồn kho hiện tại: " + productToUpdate.getQuantity());
        
        int soLuongGiam = InputHelper.nhapSo(scanner, "Nhập số lượng muốn giảm bớt");

        if (soLuongGiam > productToUpdate.getQuantity()) {
            System.out.println("-> LỖI: Số lượng giảm vượt quá tồn kho hiện có (" + productToUpdate.getQuantity() + ")!");
            return;
        }

        productToUpdate.setQuantity(productToUpdate.getQuantity() - soLuongGiam);
        System.out.println("-> Cập nhật thành công! Tồn kho mới của '" + productToUpdate.getName() + "': " + productToUpdate.getQuantity());

        FileManager.ghiFile(productList);
        System.out.println("-> ĐÃ LƯU THAY ĐỔI VÀO TỆP!");
    }

    public void banHang(Scanner scanner) {
        System.out.println("\n--- TẠO HÓA ĐƠN BÁN HÀNG ---");
        String maSP = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm khách muốn mua");

        Product productToBuy = null;
        for (Product p : productList) {
            if (p.getId().equalsIgnoreCase(maSP)) {
                productToBuy = p;
                break;
            }
        }

        if (productToBuy == null) {
            System.out.println("-> LỖI: Không tìm thấy sản phẩm có mã '" + maSP + "' trong kho!");
            return;
        }

        int soLuongMua = InputHelper.nhapSo(scanner, "Nhập số lượng khách mua");
        if (soLuongMua > productToBuy.getQuantity()) {
            System.out.println("-> LỖI: Trong kho chỉ còn " + productToBuy.getQuantity() + " sản phẩm. Không đủ để bán!");
            return;
        }

        productToBuy.setQuantity(productToBuy.getQuantity() - soLuongMua);
        FileManager.ghiFile(productList);

        Invoice hoaDon = new Invoice();
        OrderItem monHang = new OrderItem(productToBuy, soLuongMua);
        hoaDon.themMonHang(monHang);
        hoaDon.inHoaDon();
        
        System.out.println("-> BÁN HÀNG THÀNH CÔNG! Đã trừ tồn kho và cập nhật tệp.");
    }
}