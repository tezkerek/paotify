package entities;

import java.time.LocalDate;

public record Concert(int id, String location, LocalDate date, Artist artist) implements DatabaseEntity {
    public Concert(String location, LocalDate date, Artist artist) {
        this(0, location, date, artist);
    }

    @Override
    public int getId() {
        return id();
    }

    @Override
    public String toString() {
        return location + " " + date;
    }
}
