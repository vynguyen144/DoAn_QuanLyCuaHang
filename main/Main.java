package main;

import managers.StoreManager;
import utils.InputHelper;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StoreManager store = new StoreManager();
        
        while (true) {
            System.out.println("\n===== PHẦN MỀM QUẢN LÝ BÁN LẺ =====");
            System.out.println("1. Thêm sản phẩm mới vào kho");
            System.out.println("2. Hiển thị danh sách sản phẩm");
            System.out.println("3. Thoát chương trình");
            
            int choice = InputHelper.nhapSoNguyenDuong(scanner, "=> Vui lòng chọn chức năng (1-3): ");

            switch (choice) {
                case 1:
                    store.themSanPham(scanner);
                    break;
                case 2:
                    store.hienThiDanhSach();
                    break;
                case 3:
                    System.out.println("Cảm ơn bạn đã sử dụng phần mềm!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
            }
        }
    }
}