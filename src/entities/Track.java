package entities;

import java.time.Duration;

public class Track {
    private final int number;
    private final String title;
    private final Duration duration;
    private String lyrics = "";
    private final Album album;

    public Track(int number, String title, Duration duration, Album album) {
        this.number = number;
        this.title = title;
        this.duration = duration;
        this.album = album;
    }

    public Track(int number, String title, Duration duration) {
        this(number, title, duration, null);
    }

    public String getTitle() {
        return title;
    }

    public Duration getDuration() {
        return duration;
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

    @Override
    public String toString() {
        return String.format("%s", title);
    }

    public String toLongString() {
        return String.format("%s - %s", title, getAlbum().getArtist().getName());
    }
}
