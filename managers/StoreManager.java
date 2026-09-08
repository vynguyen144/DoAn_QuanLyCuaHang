package managers;

import models.Product;
import models.Food;
import models.Electronics;
import models.Clothing;
import utils.InputHelper;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreManager {
    private ArrayList<Product> productList = new ArrayList<>();

    public void themSanPham(Scanner scanner) {
        System.out.println("\nChọn loại sản phẩm muốn thêm:");
        System.out.println("1. Thực phẩm  |  2. Điện tử  |  3. Quần áo");
        int loai = InputHelper.nhapSoNguyenDuong(scanner, "=> Chọn (1-3): ");

        Product newProduct = null;
        if (loai == 1) newProduct = new Food();
        else if (loai == 2) newProduct = new Electronics();
        else if (loai == 3) newProduct = new Clothing();
        else {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        newProduct.nhapThongTin(scanner);
        productList.add(newProduct);
        System.out.println("-> THÊM SẢN PHẨM THÀNH CÔNG!\n");
    }

    public void hienThiDanhSach() {
        if (productList.isEmpty()) {
            System.out.println("\n-> Kho hàng đang trống!");
            return;
        }
        System.out.println("\n=== DANH SÁCH SẢN PHẨM TRONG KHO ===");
        for (Product p : productList) {
            p.xuatThongTin();
        }
    }
}