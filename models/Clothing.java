package models;

import utils.InputHelper;
import java.util.Scanner;

public class Clothing extends Product {
    private String size;

    public Clothing() {}

    @Override
    public void nhapThongTin(Scanner scanner) {
        System.out.println("\n--- NHẬP QUẦN ÁO ---");
        super.nhapThongTin(scanner);
        this.size = InputHelper.nhapChuoi(scanner, "Nhập kích cỡ (S/M/L/XL)");
    }

    @Override
    public double calculateFinalPrice() {
        // Đa hình: Nếu là size XL hoặc XXL thì phụ thu thêm 10%
        if (size.equalsIgnoreCase("XL") || size.equalsIgnoreCase("XXL")) {
            return basePrice + (basePrice * 0.1);
        }
        return basePrice;
    }

    @Override
    public void xuatThongTin() {
        System.out.printf("[Quần áo]   Mã: %s | Tên: %s | SL: %d | Giá gốc: %d | Size: %s | Giá bán: %.0f VNĐ\n",
                id, name, quantity, basePrice, size.toUpperCase(), calculateFinalPrice());
    }
}