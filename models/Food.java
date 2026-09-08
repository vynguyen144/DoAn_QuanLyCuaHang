package models;

import java.util.Scanner;
import utils.InputHelper;

public class Food extends Product {
    private int expiryDays;

    @Override
    public void nhapThongTin(Scanner scanner) {
        System.out.println("\n--- NHẬP THỰC PHẨM ---");
        super.nhapThongTin(scanner);
        this.expiryDays = InputHelper.nhapSoNguyenDuong(scanner, "Nhập số ngày còn hạn: ");
    }

    @Override
    public double calculateFinalPrice() {
        if (this.expiryDays <= 3) {
            return this.basePrice * 0.5; // Giảm 50% nếu cận date
        }
        return this.basePrice;
    }

    @Override
    public void xuatThongTin() {
        super.xuatThongTin();
        System.out.printf(" | Hạn SD: %d ngày | Giá bán: %.0f VNĐ\n", expiryDays, calculateFinalPrice());
    }
}