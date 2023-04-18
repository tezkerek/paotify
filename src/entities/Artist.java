package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Artist {
    private final String name;
    private final List<Album> albums = new ArrayList<>();

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Album> getAlbums() {
        return Collections.unmodifiableList(albums);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public String toString() {
        return name;
    }
}
