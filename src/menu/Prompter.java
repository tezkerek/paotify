package menu;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

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

    public int promptInt(String prompt) throws NumberFormatException {
        return Integer.parseInt(promptString(prompt));
    }

    protected static final String DURATION_REGEX = "(\\d+):(\\d+)";
    Pattern durationPattern = Pattern.compile(DURATION_REGEX);

    protected Duration promptDuration(String prompt) throws PromptException {
        var input = promptString(prompt);
        var matcher = durationPattern.matcher(input);
        if (!matcher.find()) {
            throw new PromptException("Invalid duration");
        }
        var minutes = Integer.parseInt(matcher.group(1));
        var seconds = Integer.parseInt(matcher.group(2));
        return Duration.ofSeconds(minutes * 60L + seconds);
    }

    public LocalDate promptDate(String prompt) {
        var dateStr = promptString(prompt);
        return LocalDate.parse(dateStr);
    }
}