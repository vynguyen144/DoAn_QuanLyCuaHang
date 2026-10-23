package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Resolves a writable location for user data, outside the application folder.
 * Existing repository/test data is intentionally not imported into the desktop app.
 */
public final class AppPaths {
    private static final String APP_FOLDER = "QuanLyCuaHang";
    private static final String DATA_FILE = "store_data.txt";

    private AppPaths() {
    }

    public static Path getDataFile() throws IOException {
        Path dataDirectory = getDataDirectory();
        Files.createDirectories(dataDirectory);
        return dataDirectory.resolve(DATA_FILE);
    }

    public static Path getDataDirectory() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            if (localAppData != null && !localAppData.isBlank()) {
                return Path.of(localAppData, APP_FOLDER);
            }
        }

        String userHome = System.getProperty("user.home");
        return Path.of(userHome, ".local", "share", APP_FOLDER);
    }
}
