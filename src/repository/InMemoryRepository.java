package repository;

import entities.Album;
import entities.Artist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryRepository implements Repository {
    List<Artist> artists = new ArrayList<>();

    @Override
    public List<Artist> getArtists() {
        return Collections.unmodifiableList(artists);
    }

    @Override
    public List<Album> getAlbums() {
        return getArtists().stream().flatMap(artist -> artist.getAlbums().stream()).toList();
    }

    @Override
    public void addArtist(Artist artist) {
        artists.add(artist);
    }
}
