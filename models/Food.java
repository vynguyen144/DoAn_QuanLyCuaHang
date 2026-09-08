package models;

import utils.InputHelper;
import java.util.Scanner;

public class Food extends Product {
    private int expiryDays;

    public Food() {}

    @Override
    public void nhapThongTin(Scanner scanner) {
        System.out.println("\n--- NHẬP THỰC PHẨM ---");
        super.nhapThongTin(scanner); // Gọi cha để nhập Mã, Tên, SL, Giá trước
        this.expiryDays = InputHelper.nhapSo(scanner, "Nhập số ngày còn hạn");
    }

    @Override
    public double calculateFinalPrice() {
        // Đa hình: Dưới 7 ngày thì giảm giá 50%
        if (expiryDays < 7) {
            return basePrice * 0.5;
        }
        return basePrice;
    }

    @Override
    public void xuatThongTin() {
        System.out.printf("[Thực phẩm] Mã: %s | Tên: %s | SL: %d | Giá gốc: %d | Còn hạn: %d ngày | Giá bán: %.0f VNĐ\n",
                id, name, quantity, basePrice, expiryDays, calculateFinalPrice());
    }
}