package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    public String toString() {
        return String.format("%s (%d tracks)", name, tracks.size());
    }
}
