package models;

import java.util.Scanner;
import utils.InputHelper;

public abstract class Product {
    protected String id;
    protected String name;
    protected int quantity;
    protected int basePrice;

    public Product() {}

    public void nhapThongTin(Scanner scanner) {
        this.id = InputHelper.nhapChuoi(scanner, "Nhập mã sản phẩm");
        this.name = InputHelper.nhapChuoi(scanner, "Nhập tên sản phẩm");
        this.quantity = InputHelper.nhapSo(scanner, "Nhập số lượng");
        this.basePrice = InputHelper.nhapSo(scanner, "Nhập giá gốc (VNĐ)");
    }

    public abstract double calculateFinalPrice();
    public abstract void xuatThongTin();

    // --- CÁC HÀM GETTER / SETTER ĐỂ HỖ TRỢ BÁN HÀNG ---
    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public int getQuantity() { return this.quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public int getBasePrice() { return this.basePrice; }
    public void setBasePrice(int basePrice) { this.basePrice = basePrice; }
}