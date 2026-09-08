package models;

import java.util.Scanner;

public class Clothing extends Product {
    private String size;

    @Override
    public void nhapThongTin(Scanner scanner) {
        System.out.println("\n--- NHẬP QUẦN ÁO ---");
        super.nhapThongTin(scanner);
        System.out.print("Nhập Size (S/M/L/XL): ");
        this.size = scanner.nextLine().toUpperCase();
    }

    @Override
    public double calculateFinalPrice() {
        if (this.size.equals("XL")) {
            return this.basePrice * 1.1; // Size XL phụ thu 10% vải
        }
        return this.basePrice;
    }

    @Override
    public void xuatThongTin() {
        super.xuatThongTin();
        System.out.printf(" | Size: %s | Giá bán: %.0f VNĐ\n", size, calculateFinalPrice());
    }
}