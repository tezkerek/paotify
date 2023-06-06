package menu;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Prompter {
    Scanner scanner;
    PrintStream out;

    public Prompter(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public String promptString(String prompt) {
        out.print(prompt);
        return scanner.nextLine();
    }

    public String promptString(String prompt, String defaultValue) {
        out.print(prompt);
        var value = scanner.nextLine();
        return value.equals("") ? defaultValue : value;
    }

    public int promptInt(String prompt) throws NumberFormatException {
        return Integer.parseInt(promptString(prompt));
    }

    public List<Integer> promptInts(String prompt) throws NumberFormatException, PromptException {
        var input = promptString(prompt);
        return Arrays.stream(input.split(",")).flatMap(token -> {
            List<Integer> rangePair = Arrays.stream(token.split("-")).map(Integer::parseInt).toList();
            return switch (rangePair.size()) {
                case 1 -> Stream.of(Integer.parseInt(token));
                case 2 -> IntStream.rangeClosed(rangePair.get(0), rangePair.get(1)).boxed().toList().stream();
                default -> throw new PromptException("Invalid range");
            };
        }).toList();
    }

    protected static final String DURATION_REGEX = "(\\d+):(\\d+)";
    Pattern durationPattern = Pattern.compile(DURATION_REGEX);

    protected Duration parseDuration(String input) throws PromptException {
        var matcher = durationPattern.matcher(input);
        if (!matcher.find()) {
            throw new PromptException("Invalid duration");
        }
        var minutes = Integer.parseInt(matcher.group(1));
        var seconds = Integer.parseInt(matcher.group(2));
        return Duration.ofSeconds(minutes * 60L + seconds);
    }

    protected Duration promptDuration(String prompt) throws PromptException {
        var input = promptString(prompt);
        return parseDuration(input);
    }

    protected Duration promptDuration(String prompt, Duration defaultValue) throws PromptException {
        var input = promptString(prompt, formatDuration(defaultValue));
        return parseDuration(input);
    }

    public LocalDate promptDate(String prompt) throws DateTimeParseException {
        var dateStr = promptString(prompt);
        return LocalDate.parse(dateStr);
    }

    public LocalDate promptDate(String prompt, LocalDate defaultValue) throws DateTimeParseException {
        var dateStr = promptString(prompt, defaultValue.toString());
        return LocalDate.parse(dateStr);
    }

    public static String formatDuration(Duration duration) {
        return "%d:%d".formatted(duration.toMinutes(), duration.toSecondsPart());
    }
}