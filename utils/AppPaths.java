package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Resolves the writable data location for the application.
 * User data is kept outside the application folder so packaged builds can
 * run from read-only locations and upgrades do not overwrite inventory.
 */
public final class AppPaths {
    private static final String APP_FOLDER = "QuanLyCuaHang";
    private static final String DATA_FILE = "store_data.txt";

    private AppPaths() {
    }

    public static Path getDataFile() throws IOException {
        Path dataDirectory = getDataDirectory();
        Files.createDirectories(dataDirectory);

        Path destination = dataDirectory.resolve(DATA_FILE);
        if (Files.exists(destination)) {
            return destination;
        }

        // One-time migration for users running the old version from a folder
        // containing store_data.txt. Never overwrite an existing destination.
        Path legacyFile = Path.of(DATA_FILE).toAbsolutePath().normalize();
        if (Files.isRegularFile(legacyFile) && !legacyFile.equals(destination)) {
            Files.copy(legacyFile, destination, StandardCopyOption.COPY_ATTRIBUTES);
        }

        return destination;
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
