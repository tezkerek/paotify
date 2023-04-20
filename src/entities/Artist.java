package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Artist {
    private final String name;
    private final List<Album> albums = new ArrayList<>();
    private final List<Concert> concerts = new ArrayList<>();

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Album> getAlbums() {
        return Collections.unmodifiableList(albums);
    }

    public List<Concert> getConcerts() {
        return Collections.unmodifiableList(concerts);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    public void addConcert(Concert concert) {
        concerts.add(concert);
    }

    public String toString() {
        return name;
    }
}
