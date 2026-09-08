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

        product.nhapThongTin(scanner);
        productList.add(product);
        
        FileManager.ghiFile(productList);
        System.out.println("-> Thêm sản phẩm thành công!");
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
            if (p.getId().equals(maSP)) {
                productToEdit = p;
                break;
            }
        }

        if (productToEdit == null) {
            System.out.println("-> LỖI: Không tìm thấy sản phẩm có mã '" + maSP + "' trong kho!");
            return;
        }

        System.out.println("-> Đã tìm thấy sản phẩm! Vui lòng nhập thông tin mới:");
        productToEdit.nhapThongTin(scanner);
        
        FileManager.ghiFile(productList);
        System.out.println("-> CẬP NHẬT THÀNH CÔNG VÀ ĐÃ LƯU VÀO TỆP!");
    }

    public void xoaSanPham(Scanner scanner) {
        System.out.println("\n--- XÓA SẢN PHẨM KHỎI KHO ---");
        String maSP = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm cần xóa");

        // Tìm và xóa sản phẩm nếu trùng mã
        boolean removed = productList.removeIf(p -> p.getId().equals(maSP));

        if (removed) {
            FileManager.ghiFile(productList);
            System.out.println("-> XÓA THÀNH CÔNG VÀ ĐÃ CẬP NHẬT TỆP!");
        } else {
            System.out.println("-> LỖI: Không tìm thấy sản phẩm có mã '" + maSP + "' trong kho!");
        }
    }

    public void banHang(Scanner scanner) {
        System.out.println("\n--- TẠO HÓA ĐƠN BÁN HÀNG ---");
        String maSP = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm khách muốn mua");

        Product productToBuy = null;
        for (Product p : productList) {
            if (p.getId().equals(maSP)) {
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