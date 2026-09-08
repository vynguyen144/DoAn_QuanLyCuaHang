# Đồ Án Lập Trình Hướng Đối Tượng: Quản Lý Cửa Hàng Bán Lẻ

## Giới thiệu
Đây là dự án phần mềm quản lý kho hàng cho một cửa hàng bán lẻ tổng hợp. Chương trình được xây dựng bằng ngôn ngữ **Java**, áp dụng chặt chẽ các nguyên lý của Lập trình Hướng đối tượng (OOP) để giải quyết bài toán quản lý nhiều danh mục sản phẩm khác nhau.

## Các tính năng nổi bật
* **Thêm mới sản phẩm đa hình:** Hỗ trợ nhập liệu cho Thực phẩm, Đồ điện tử và Quần áo với các thuộc tính chuyên biệt.
* **Xử lý ngoại lệ an toàn:** Hệ thống chặn lỗi nhập sai kiểu dữ liệu (nhập chữ vào ô số) và chặn số âm, đảm bảo chương trình hoạt động liên tục không bị gián đoạn (crash).
* **Tính toán giá động:** Tự động tính giá bán thực tế dựa trên logic riêng của từng loại mặt hàng (Ví dụ: Giảm 50% cho thực phẩm cận date, cộng 5% phí bảo hành cho đồ điện tử).

## 4 Tính chất OOP được áp dụng
1. **Đóng gói (Encapsulation):** Bảo vệ dữ liệu nội bộ của các lớp bằng Access Modifier (`private`, `protected`) và tương tác qua các phương thức an toàn.
2. **Kế thừa (Inheritance):** Các lớp `Food`, `Electronics`, `Clothing` kế thừa toàn bộ thuộc tính cơ sở từ lớp cha `Product`.
3. **Đa hình (Polymorphism):** Cài đặt ghi đè (Overriding) phương thức `calculateFinalPrice()` và `xuatThongTin()` cho từng lớp con. Quản lý chung bằng một danh sách `ArrayList<Product>`.
4. **Trừu tượng (Abstraction):** Sử dụng lớp trừu tượng `Product` chứa phương thức thuần ảo để làm khuôn mẫu chuẩn cho các danh mục sản phẩm.

## Thông tin sinh viên
* **Sinh viên thực hiện:**
Nguyễn Thị Thanh Vy - NBS2503ITA0015
Huỳnh Sĩ An Khang - NBS2503ITA0007
Đỗ Quan Đạt - NBS2603ITA0012
Nguyễn Hoàng Hữu Đức - NBS2503ITA0012
* **Ngôn ngữ phát triển:** Java
- Ngôn ngữ phát triển: Java

## 📊 Sơ đồ thiết kế lớp (UML Class Diagram)

```mermaid
classDiagram
    class StoreManager {
        -ArrayList~Product~ productList
        +themSanPham(scanner: Scanner)
        +hienThiDanhSach()
    }

    class Product {
        <<abstract>>
        #String id
        #String name
        #int quantity
        #int basePrice
        +nhapThongTin(scanner: Scanner)
        +xuatThongTin()
        +calculateFinalPrice() double
    }

    class Food {
        -int expiryDays
        +nhapThongTin(scanner: Scanner)
        +xuatThongTin()
        +calculateFinalPrice() double
    }

    class Electronics {
        -int warrantyMonths
        +nhapThongTin(scanner: Scanner)
        +xuatThongTin()
        +calculateFinalPrice() double
    }

    class Clothing {
        -String size
        +nhapThongTin(scanner: Scanner)
        +xuatThongTin()
        +calculateFinalPrice() double
    }

    StoreManager o-- Product : Quản lý
    Product <|-- Food : Kế thừa
    Product <|-- Electronics : Kế thừa
    Product <|-- Clothing : Kế thừa
