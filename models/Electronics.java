package models;

import java.util.Scanner;
import utils.InputHelper;

public class Electronics extends Product {
    private int warrantyMonths;

    @Override
    public void nhapThongTin(Scanner scanner) {
        System.out.println("\n--- NHẬP ĐỒ ĐIỆN TỬ ---");
        super.nhapThongTin(scanner);
        this.warrantyMonths = InputHelper.nhapSoNguyenDuong(scanner, "Nhập số tháng bảo hành: ");
    }

    @Override
    public double calculateFinalPrice() {
        return this.basePrice + (this.basePrice * 0.05); // Phí bảo hành 5%
    }

    @Override
    public void xuatThongTin() {
        super.xuatThongTin();
        System.out.printf(" | Bảo hành: %d tháng | Giá bán: %.0f VNĐ\n", warrantyMonths, calculateFinalPrice());
    }
}