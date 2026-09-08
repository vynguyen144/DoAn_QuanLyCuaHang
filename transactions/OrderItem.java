package transactions;

import models.Product;

public class OrderItem {
    private Product product; // Lưu lại xem khách mua mặt hàng nào
    private int buyQuantity; // Số lượng khách mua

    public OrderItem(Product product, int buyQuantity) {
        this.product = product;
        this.buyQuantity = buyQuantity;
    }

    // Tính thành tiền của món này (Có tính cả đa hình giảm giá)
    public double getThanhTien() {
        return product.calculateFinalPrice() * buyQuantity;
    }

    public void inChiTiet() {
        System.out.printf("- %s (Mã: %s) | SL: %d | Đơn giá: %.0f | Thành tiền: %.0f VNĐ\n",
            product.getName(), product.getId(), buyQuantity, product.calculateFinalPrice(), getThanhTien());
    }
}