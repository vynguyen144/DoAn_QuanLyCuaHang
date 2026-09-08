package models;

import utils.InputHelper;
import java.util.Scanner;

public class Electronics extends Product {
    private int warrantyMonths;

    public Electronics() {}

    @Override
    public void nhapThongTin(Scanner scanner) {
        System.out.println("\n--- NHẬP ĐỒ ĐIỆN TỬ ---");
        super.nhapThongTin(scanner); 
        this.warrantyMonths = InputHelper.nhapSo(scanner, "Nhập số tháng bảo hành");
    }

    @Override
    public double calculateFinalPrice() {
        // Đa hình: Đồ điện tử cộng thêm 5% phí bảo hành vào giá gốc
        return basePrice + (basePrice * 0.05);
    }

    @Override
    public void xuatThongTin() {
        System.out.printf("[Điện tử]   Mã: %s | Tên: %s | SL: %d | Giá gốc: %d | Bảo hành: %d tháng | Giá bán: %.0f VNĐ\n",
                id, name, quantity, basePrice, warrantyMonths, calculateFinalPrice());
    }
}