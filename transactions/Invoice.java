package transactions;

import java.util.ArrayList;

public class Invoice {
    // Mối quan hệ Kết tập: Hóa đơn chứa một danh sách các món hàng
    private ArrayList<OrderItem> danhSachMua = new ArrayList<>();
    private double tongTien = 0;

    public void themMonHang(OrderItem item) {
        danhSachMua.add(item);
        tongTien += item.getThanhTien(); // Cộng dồn tiền
    }

    public void inHoaDon() {
        System.out.println("\n================ HÓA ĐƠN BÁN HÀNG ================");
        for (OrderItem item : danhSachMua) {
            item.inChiTiet();
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("TỔNG TIỀN THANH TOÁN: %.0f VNĐ\n", tongTien);
        System.out.println("==================================================\n");
    }
}