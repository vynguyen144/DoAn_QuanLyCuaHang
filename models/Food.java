package models;

import java.util.Scanner;
import utils.InputHelper;

public class Food extends Product {
    private int expiryDays;

    public Food() {
        super();
    }

    public int getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(int expiryDays) {
        this.expiryDays = expiryDays;
    }

    @Override
    public void nhapThongTin(Scanner scanner) {
        super.nhapThongTin(scanner);
        this.expiryDays = InputHelper.nhapSo(scanner, "Nhập số ngày còn hạn sử dụng");
    }

    @Override
    public double calculateFinalPrice() {
        if (expiryDays <= 1) {
            return basePrice * 0.5; // Giảm 50% nếu cận date
        }
        return basePrice;
    }

    @Override
    public void xuatThongTin() {
        System.out.println("[Thực phẩm] Mã: " + id + " | Tên: " + name + " | SL: " + quantity + " | Giá gốc: " + basePrice + " | Còn hạn: " + expiryDays + " ngày | Giá bán: " + (long)calculateFinalPrice() + " VNĐ");
    }
}