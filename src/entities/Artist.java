package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Artist implements DatabaseEntity {
    private final int id;
    private final String name;
    private final List<Album> albums = new ArrayList<>();
    private final List<Concert> concerts = new ArrayList<>();

    public Artist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(String name) {
        this(0, name);
    }

    @Override
    public int getId() {
        return id;
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
