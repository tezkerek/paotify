package entities;

import menu.Prompter;

import java.time.Duration;

public class Track implements DatabaseEntity {
    private final int id;
    private final int number;
    private final String title;
    private final Duration duration;
    private String lyrics = "";
    private final Album album;

    public Track(int id, int number, String title, Duration duration, Album album) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.duration = duration;
        this.album = album;
    }

    public Track(int number, String title, Duration duration, Album album) {
        this(0, number, title, duration, album);
    }

    @Override
    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public Duration getDuration() {
        return duration;
    }

    public String getFormattedDuration() {
        return Prompter.formatDuration(duration);
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Album getAlbum() {
        return album;
    }

    public Artist getArtist() {
        return getAlbum().getArtist();
    }

    @Override
    public String toString() {
        return String.format("%s", title);
    }

    public String toLongString() {
        return String.format("%s - %s", title, getArtist().getName());
    }
}
