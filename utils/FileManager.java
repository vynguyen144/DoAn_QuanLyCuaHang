package utils;

import models.Clothing;
import models.Electronics;
import models.Food;
import models.Product;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Reads and writes product data.
 *
 * Current format is tab-separated with text fields encoded as URL-safe Base64,
 * so names and IDs can safely contain commas, tabs, or Vietnamese characters.
 * Legacy comma-separated rows are still accepted for backward compatibility.
 */
public final class FileManager {
    private static final String FORMAT_MARKER = "V3";

    private FileManager() {
    }

    public static void ghiFile(ArrayList<Product> productList) {
        Path dataFile;
        Path temporaryFile = null;
        try {
            dataFile = AppPaths.getDataFile();
            temporaryFile = Files.createTempFile(dataFile.getParent(), "store_data-", ".tmp");

            List<String> lines = new ArrayList<>();
            for (Product product : productList) {
                String type = getType(product);
                if (type == null) {
                    continue;
                }

                String extra = "";
                if (product instanceof Food) {
                    extra = Integer.toString(((Food) product).getExpiryDays());
                } else if (product instanceof Electronics) {
                    extra = Integer.toString(((Electronics) product).getWarrantyMonths());
                } else if (product instanceof Clothing) {
                    extra = encode(((Clothing) product).getSize() == null ? "" : ((Clothing) product).getSize());
                }

                lines.add(String.join("\t",
                        FORMAT_MARKER,
                        type,
                        encode(product.getId() == null ? "" : product.getId()),
                        encode(product.getName() == null ? "" : product.getName()),
                        Integer.toString(product.getQuantity()),
                        Integer.toString(product.getBasePrice()),
                        extra,
                        product.getReceivedDate() == null ? java.time.LocalDate.now().toString() : product.getReceivedDate().toString()));
            }

            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, dataFile, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("-> [HỆ THỐNG] Đã lưu dữ liệu tại: " + dataFile);
        } catch (IOException e) {
            System.err.println("-> LỖI KHI GHI FILE: " + e.getMessage());
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                    // Keep the original error as the useful diagnostic.
                }
            }
        }
    }

    public static ArrayList<Product> docFile() {
        ArrayList<Product> productList = new ArrayList<>();
        try {
            Path file = AppPaths.getDataFile();
            if (!Files.exists(file)) {
                return productList;
            }

            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            int lineNumber = 0;
            for (String line : lines) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }

                try {
                    Product product = line.startsWith(FORMAT_MARKER + "\t")
                            ? parseCurrentFormat(line)
                            : line.startsWith("V2\t") ? parseV2Format(line) : parseLegacyFormat(line);
                    if (product != null) {
                        productList.add(product);
                    }
                } catch (RuntimeException e) {
                    System.err.println("-> Bỏ qua dòng dữ liệu không hợp lệ " + lineNumber
                            + " trong " + file.getFileName() + ": " + e.getMessage());
                }
            }
            System.out.println("-> [HỆ THỐNG] Đã đọc dữ liệu từ: " + file);
        } catch (IOException e) {
            System.err.println("-> LỖI KHI ĐỌC FILE: " + e.getMessage());
        }
        return productList;
    }

    private static Product parseCurrentFormat(String line) {
        String[] parts = line.split("\\t", -1);
        if (parts.length != 8 || !FORMAT_MARKER.equals(parts[0])) {
            throw new IllegalArgumentException("Sai số lượng trường ở định dạng V3");
        }

        Product product = createProduct(parts[1]);
        if (product == null) {
            return null;
        }

        product.setId(decode(parts[2]));
        product.setName(decode(parts[3]));
        product.setQuantity(Integer.parseInt(parts[4]));
        product.setBasePrice(Integer.parseInt(parts[5]));

        if (product instanceof Food) {
            ((Food) product).setExpiryDays(Integer.parseInt(parts[6]));
        } else if (product instanceof Electronics) {
            ((Electronics) product).setWarrantyMonths(Integer.parseInt(parts[6]));
        } else if (product instanceof Clothing) {
            ((Clothing) product).setSize(decode(parts[6]));
        }
        product.setReceivedDate(java.time.LocalDate.parse(parts[7]));
        return product;
    }

    private static Product parseV2Format(String line) {
        String[] parts = line.split("\\t", -1);
        if (parts.length != 7 || !"V2".equals(parts[0])) throw new IllegalArgumentException("Sai số lượng trường ở định dạng V2");
        Product product = createProduct(parts[1]);
        if (product == null) return null;
        product.setId(decode(parts[2]));
        product.setName(decode(parts[3]));
        product.setQuantity(Integer.parseInt(parts[4]));
        product.setBasePrice(Integer.parseInt(parts[5]));
        if (product instanceof Food) ((Food) product).setExpiryDays(Integer.parseInt(parts[6]));
        else if (product instanceof Electronics) ((Electronics) product).setWarrantyMonths(Integer.parseInt(parts[6]));
        else if (product instanceof Clothing) ((Clothing) product).setSize(decode(parts[6]));
        product.setReceivedDate(java.time.LocalDate.now());
        return product;
    }

    private static Product parseLegacyFormat(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Dòng cũ không đủ trường");
        }

        Product product = createProduct(parts[0].trim());
        if (product == null) {
            return null;
        }

        product.setId(parts[1]);
        product.setName(parts[2]);
        product.setQuantity(Integer.parseInt(parts[3].trim()));
        product.setBasePrice(Integer.parseInt(parts[4].trim()));

        // The old format never saved these fields, so their original values
        // cannot be reconstructed. Keep safe defaults until the user edits them.
        if (product instanceof Food) {
            ((Food) product).setExpiryDays(0);
        } else if (product instanceof Electronics) {
            ((Electronics) product).setWarrantyMonths(0);
        } else if (product instanceof Clothing) {
            ((Clothing) product).setSize("");
        }
        return product;
    }

    private static Product createProduct(String type) {
        switch (type) {
            case "FOOD":
                return new Food();
            case "ELEC":
                return new Electronics();
            case "CLOTH":
                return new Clothing();
            default:
                return null;
        }
    }

    private static String getType(Product product) {
        if (product instanceof Food) {
            return "FOOD";
        }
        if (product instanceof Electronics) {
            return "ELEC";
        }
        if (product instanceof Clothing) {
            return "CLOTH";
        }
        return null;
    }

    private static String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
