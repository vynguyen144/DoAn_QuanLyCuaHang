package utils;

import models.Clothing;
import models.Electronics;
import models.Food;
import models.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
    private static final String FILE_NAME = "store_data.txt";

    public static void ghiFile(ArrayList<Product> productList) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Product p : productList) {
                String type = "";
                if (p instanceof Food) {
                    type = "FOOD";
                } else if (p instanceof Electronics) {
                    type = "ELEC";
                } else if (p instanceof Clothing) {
                    type = "CLOTH";
                }
                writer.write(type + "," + p.getId() + "," + p.getName() + "," + p.getQuantity() + "," + p.getBasePrice() + "\n");
            }
            System.out.println("-> [HỆ THỐNG] Đã lưu dữ liệu vào tệp thành công!");
        } catch (IOException e) {
            System.out.println("-> LỖI KHI GHI FILE: " + e.getMessage());
        }
    }

    public static ArrayList<Product> docFile() {
        ArrayList<Product> productList = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return productList;
        }

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String type = parts[0];
                    String id = parts[1];
                    String name = parts[2];
                    int quantity = Integer.parseInt(parts[3]);
                    int basePrice = Integer.parseInt(parts[4]);

                    Product p = null;
                    if (type.equals("FOOD")) {
                        p = new Food();
                    } else if (type.equals("ELEC")) {
                        p = new Electronics();
                    } else if (type.equals("CLOTH")) {
                        p = new Clothing();
                    }

                    if (p != null) {
                        p.setId(id);
                        p.setName(name);
                        p.setQuantity(quantity);
                        p.setBasePrice(basePrice);
                        productList.add(p);
                    }
                }
            }
            System.out.println("-> [HỆ THỐNG] Đã tải dữ liệu cũ từ tệp lên kho!");
        } catch (Exception e) {
            System.out.println("-> LỖI KHI ĐỌC FILE: " + e.getMessage());
        }
        return productList;
    }
}