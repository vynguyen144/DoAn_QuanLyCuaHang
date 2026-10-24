package ui;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Creates a one-page PDF invoice from a saved sales-history row. */
public final class InvoiceExporter {
    private InvoiceExporter() { }

    public static void export(String[] sale, File output) throws IOException {
        if (sale == null || sale.length < 6) {
            throw new IllegalArgumentException("Dữ liệu giao dịch không hợp lệ.");
        }

        try (PDDocument document = new PDDocument()) {
            PDFont font = loadUnicodeFont(document);
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                float y = 750;
                y = write(stream, font, 20, y, "HOA DON BAN HANG", 19);
                y -= 12;
                y = write(stream, font, 11, y, "Store Desk - Quan ly cua hang", 11);
                y -= 18;
                y = write(stream, font, 11, y, "Ma giao dich: " + sale[1], 11);
                y = write(stream, font, 11, y, "Thoi gian: " + sale[0], 11);
                y -= 18;
                y = write(stream, font, 12, y, "THONG TIN SAN PHAM", 12);
                y -= 5;
                y = write(stream, font, 11, y, "Ma san pham: " + sale[1], 11);
                y = write(stream, font, 11, y, "Ten san pham: " + sale[2], 11);
                y = write(stream, font, 11, y, "So luong: " + sale[3], 11);
                y = write(stream, font, 11, y, "Don gia: " + money(Long.parseLong(sale[4])), 11);
                y -= 12;
                y = write(stream, font, 14, y, "TONG THANH TOAN: " + money(Long.parseLong(sale[5])), 14);
                y -= 24;
                write(stream, font, 10, y, "Cam on quy khach va hen gap lai!", 10);
            }
            document.save(output);
        } catch (NumberFormatException ex) {
            throw new IOException("Số tiền hoặc số lượng trong giao dịch không hợp lệ.", ex);
        }
    }

    private static float write(PDPageContentStream stream, PDFont font, float x, float y,
                               String text, float size) throws IOException {
        List<String> lines = wrap(text, 82);
        for (String line : lines) {
            stream.beginText();
            stream.setFont(font, size);
            stream.newLineAtOffset(x, y);
            stream.showText(line);
            stream.endText();
            y -= size + 7;
        }
        return y;
    }

    private static List<String> wrap(String text, int maxLength) {
        List<String> lines = new ArrayList<>();
        String remaining = text == null ? "" : text;
        while (remaining.length() > maxLength) {
            int cut = remaining.lastIndexOf(' ', maxLength);
            if (cut < 1) cut = maxLength;
            lines.add(remaining.substring(0, cut));
            remaining = remaining.substring(cut).trim();
        }
        lines.add(remaining);
        return lines;
    }

    private static PDFont loadUnicodeFont(PDDocument document) throws IOException {
        String[] candidates = {
                "C:/Windows/Fonts/arial.ttf",
                "C:/Windows/Fonts/calibri.ttf",
                "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
                "/usr/share/fonts/truetype/liberation2/LiberationSans-Regular.ttf"
        };
        for (String candidate : candidates) {
            File fontFile = new File(candidate);
            if (fontFile.isFile()) {
                return PDType0Font.load(document, fontFile);
            }
        }
        // Standard font fallback keeps export available on systems without a common Unicode font.
        return new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    }

    private static String money(long value) {
        return NumberFormat.getIntegerInstance(new Locale("vi", "VN")).format(value) + " VND";
    }
}
