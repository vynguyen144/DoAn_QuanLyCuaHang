package models;

import java.util.Scanner;
import utils.InputHelper;

public class Clothing extends Product {
    private String size;

    public Clothing() {
        super();
    }

    @Override
    public void nhapThongTin(Scanner scanner) {
        super.nhapThongTin(scanner);
        this.size = InputHelper.nhapChuoi(scanner, "Nhập kích cỡ (Size S/M/L/XL)");
    }

    @Override
    public double calculateFinalPrice() {
        if (size != null && (size.equalsIgnoreCase("L") || size.equalsIgnoreCase("XL"))) {
            return basePrice * 1.1;
        }
        return basePrice;
    }

    @Override
    public void xuatThongTin() {
        String safeSize = (size != null) ? size.toUpperCase() : "N/A";
        System.out.println("[Quần áo] Mã: " + id + " | Tên: " + name + " | SL: " + quantity + " | Giá gốc: " + basePrice + " | Size: " + safeSize + " | Giá bán: " + (long)calculateFinalPrice() + " VNĐ");
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
}