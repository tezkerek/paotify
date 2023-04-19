package repository;

import entities.Album;
import entities.Artist;

import java.util.List;

public interface Repository {
    List<Artist> getArtists();

    List<Album> getAlbums();

    void addArtist(Artist artist);

    void deleteArtist(Artist artist);

    void deleteAlbum(Artist artist, Album album);
}
