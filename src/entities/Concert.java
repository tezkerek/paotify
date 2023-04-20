package entities;

import java.time.LocalDate;

public record Concert(String location, LocalDate date, Artist artist) {

    @Override
    public String toString() {
        return location + " " + date;
    }
}
