package menu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final Path path;

    public Logger(String logPath) {
        this.path = Paths.get(logPath);
    }

    public void log(String value) {
        var timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        var content = "%s,%s\n".formatted(timestamp, escapeCSV(value));
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
                Files.writeString(path, "timestamp,action\n", StandardOpenOption.WRITE);
            }
            Files.writeString(path, content, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String escapeCSV(String s) {
        s = s.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }
}
