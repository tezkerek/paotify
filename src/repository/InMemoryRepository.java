package repository;

import entities.Album;
import entities.Artist;
import entities.Playlist;
import entities.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryRepository implements Repository {
    List<Artist> artists = new ArrayList<>();
    List<Playlist> playlists = new ArrayList<>();

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

    @Override
    public void deleteArtist(Artist artist) {
        artists.remove(artist);
    }

    @Override
    public void deleteAlbum(Artist artist, Album album) {
        artist.removeAlbum(album);
    }

    @Override
    public List<Track> getTracks() {
        return getArtists().stream().flatMap(artist -> artist.getAlbums().stream().flatMap(album -> album.getTracks().stream())).toList();
    }

    @Override
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    @Override
    public List<Playlist> getPlaylists() {
        return Collections.unmodifiableList(playlists);
    }
}
