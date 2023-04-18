package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {
    private final String title;
    private final LocalDate releaseDate;
    private final List<Track> tracks = new ArrayList<>();
    private final Artist artist;

    public Album(String title, LocalDate releaseDate, Artist artist) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.artist = artist;
    }

    public Album(String title, LocalDate releaseDate) {
        this(title, releaseDate, null);
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public Artist getArtist() {
        return artist;
    }

    public String toString() {
        return String.format("%s (%d)", title, releaseDate.getYear());
    }
}
