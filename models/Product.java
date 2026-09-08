package models;

import utils.InputHelper;
import java.util.Scanner;

public abstract class Product {
    protected String id;
    protected String name;
    protected int quantity;
    protected int basePrice;

    public abstract double calculateFinalPrice();

    public void nhapThongTin(Scanner scanner) {
        System.out.print("Nhập mã sản phẩm: ");
        this.id = scanner.nextLine();
        System.out.print("Nhập tên sản phẩm: ");
        this.name = scanner.nextLine();
        this.quantity = InputHelper.nhapSoNguyenDuong(scanner, "Nhập số lượng tồn kho: ");
        this.basePrice = InputHelper.nhapSoNguyenDuong(scanner, "Nhập giá bán cơ bản (VNĐ): ");
    }

    public void xuatThongTin() {
        System.out.printf("Mã: %s | Tên: %s | SL: %d | Giá gốc: %d", id, name, quantity, basePrice);
    }
}