
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class DataStorage {
    private static final Path DATA_DIRECTORY = Path.of("data");

    private DataStorage() {
    }

    public static void ensureDataDirectory() throws IOException {
        Files.createDirectories(DATA_DIRECTORY);
    }

    public static void saveTextFile(String fileName, ArrayList<String> lines)
            throws IOException {
        ensureDataDirectory();

        Path filePath = DATA_DIRECTORY.resolve(fileName);
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    public static ArrayList<String> loadTextFile(String fileName)
            throws IOException {
        ensureDataDirectory();

        Path filePath = DATA_DIRECTORY.resolve(fileName);

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                Files.readAllLines(filePath, StandardCharsets.UTF_8)
        );
    }
}

