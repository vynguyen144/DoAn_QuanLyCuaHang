package main;

import managers.StoreManager;
import utils.InputHelper;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StoreManager store = new StoreManager();
        
        while (true) {
            System.out.println("\n===== PHẦN MỀM QUẢN LÝ CỬA HÀNG BÁN LẺ =====");
            System.out.println("1. Thêm sản phẩm mới vào kho");
            System.out.println("2. Hiển thị danh sách sản phẩm");
            System.out.println("3. Sửa thông tin sản phẩm");
            System.out.println("4. Xóa sản phẩm khỏi kho");
            System.out.println("5. Bán hàng & Xuất hóa đơn");
            System.out.println("6. Thoát chương trình");
            
            int choice = InputHelper.nhapMenu(scanner, "=> Vui lòng chọn chức năng (1-6): ");

            try {
                switch (choice) {
                    case 1:
                        store.themSanPham(scanner);
                        break;
                    case 2:
                        store.hienThiDanhSach();
                        break;
                    case 3:
                        store.suaSanPham(scanner);
                        break;
                    case 4:
                        store.xoaSanPham(scanner);
                        break;
                    case 5:
                        store.banHang(scanner);
                        break;
                    case 6:
                        System.out.println("Cảm ơn bạn đã sử dụng phần mềm!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
                }
            } catch (RuntimeException e) {
                if (e.getMessage() != null && e.getMessage().equals("CANCEL_PROCESS")) {
                    System.out.println("\n[!] ĐÃ HỦY THAO TÁC. BẢO TOÀN DỮ LIỆU. QUAY LẠI MENU CHÍNH...");
                } else {
                    throw e; 
                }
            }
        }
    }
}