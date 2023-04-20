package entities;

import java.util.*;

public class Playlist {
    private final String name;
    private final List<Track> tracks = new ArrayList<>();

    public Playlist(String name, List<Track> tracks) {
        this.name = name;
        this.tracks.addAll(tracks);
    }

    public String getName() {
        return name;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public List<Artist> getArtists() {
        return tracks.stream().map(Track::getArtist).distinct().toList();
    }

    public List<Concert> getSortedConcerts() {
        return getArtists().stream()
                .flatMap(artist -> artist.getConcerts().stream())
                .sorted(Comparator.comparing(Concert::date).reversed())
                .toList();
    }

    @Override
    public String toString() {
        return String.format("%s (%d tracks)", name, tracks.size());
    }
}
