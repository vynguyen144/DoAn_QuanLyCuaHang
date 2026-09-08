package utils;

import java.util.Scanner;

public class InputHelper {
    public static int nhapSoNguyenDuong(Scanner scanner, String thongBao) {
        int so = 0;
        while (true) {
            System.out.print(thongBao);
            try {
                so = Integer.parseInt(scanner.nextLine());
                if (so < 0) {
                    System.out.println("-> LỖI: Không được nhập số âm!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("-> LỖI CÚ PHÁP: Vui lòng chỉ nhập số, không nhập chữ!");
            }
        }
        return so;
    }
}