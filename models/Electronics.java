package models;

import java.util.Scanner;
import utils.InputHelper;

public class Electronics extends Product {
    private int warrantyMonths;

    public Electronics() {
        super();
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public void nhapThongTin(Scanner scanner) {
        super.nhapThongTin(scanner);
        this.warrantyMonths = InputHelper.nhapSo(scanner, "Nhập thời gian bảo hành (tháng)");
    }

    @Override
    public double calculateFinalPrice() {
        return basePrice + (warrantyMonths * 1000); // Phụ thu phí bảo hành
    }

    @Override
    public void xuatThongTin() {
        System.out.println("[Đồ điện tử] Mã: " + id + " | Tên: " + name + " | SL: " + quantity + " | Giá gốc: " + basePrice + " | Bảo hành: " + warrantyMonths + " tháng | Giá bán: " + (long)calculateFinalPrice() + " VNĐ");
    }
}