package utils;
import java.util.Scanner;

public class InputHelper {

    public static String nhapChuoi(Scanner scanner, String thongBao) {
        System.out.print(thongBao + " (Nhập '0' để Hủy & Quay lại): ");
        String input = scanner.nextLine();
        
        if (input.equals("0")) {
            throw new RuntimeException("CANCEL_PROCESS");
        }
        return input;
    }

    public static int nhapSo(Scanner scanner, String thongBao) {
        while (true) {
            try {
                System.out.print(thongBao + " (Nhập '0' để Hủy & Quay lại): ");
                String input = scanner.nextLine();

                if (input.equals("0")) {
                    throw new RuntimeException("CANCEL_PROCESS");
                }

                int so = Integer.parseInt(input);
                if (so < 0) {
                    System.out.println("-> LỖI: Vui lòng nhập số lớn hơn hoặc bằng 0!");
                } else {
                    return so;
                }
            } catch (NumberFormatException e) {
                System.out.println("-> LỖI: Vui lòng nhập số hợp lệ (không gõ chữ)!");
            }
        }
    }

    public static int nhapMenu(Scanner scanner, String thongBao) {
        while (true) {
            try {
                System.out.print(thongBao);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("-> LỖI: Vui lòng nhập số hợp lệ!");
            }
        }
    }
}