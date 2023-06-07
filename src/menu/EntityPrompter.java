package menu;

import entities.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityPrompter extends Prompter {
    public EntityPrompter(InputStream in, PrintStream out) {
        super(in, out);
    }

    public Artist readArtist() {
        var name = promptString("Artist name: ");
        return new Artist(name);
    }

    public Artist readArtist(Artist defaultArtist) {
        var name = promptString(
            String.format("Artist name [%s]: ", defaultArtist.getName()),
            defaultArtist.getName()
        );
        return new Artist(name);
    }

    public Album readAlbum(Artist artist) {
        var title = promptString("Album title: ");
        var releaseDate = promptDate("Album release date: ");
        var genre = chooseGenre();

        return new Album(title, releaseDate, genre, artist);
    }

    public Album readAlbum(Artist artist, Album defaultAlbum) {
        var title = promptString(
            String.format("Album title[%s]: ", defaultAlbum.getTitle()),
            defaultAlbum.getTitle()
        );
        var releaseDate = promptDate(
            String.format("Album release date[%s]: ", defaultAlbum.getReleaseDate()),
            defaultAlbum.getReleaseDate()
        );
        var genre = chooseGenre();

        return new Album(title, releaseDate, genre, artist);
    }

    public Track readTrack(int number, Album album) throws PromptException {
        var title = promptString(String.format("Track #%d title: ", number));
        var duration = promptDuration(String.format("Track #%d duration: ", number));

        return new Track(number, title, duration, album);
    }

    public Track readTrack(int number, Album album, Track defaultTrack) throws PromptException {
        var title = promptString(
            "Track #%d title [%s]: ".formatted(number, defaultTrack.getTitle()),
            defaultTrack.getTitle()
        );
        var duration = promptDuration(
            "Track #%d duration [%s]: ".formatted(number, defaultTrack.getFormattedDuration()),
            defaultTrack.getDuration()
        );
        return new Track(number, title, duration, album);
    }

    public Playlist readPlaylist(List<Track> availableTracks) {
        var name = promptString("Playlist name: ");
        var tracks = chooseMultipleTracks(availableTracks);
        return new Playlist(name, tracks);
    }

    public Concert readConcert(Artist artist) {
        var location = promptString("Concert location: ");
        var date = promptDate("Concert date: ");
        return new Concert(location, date, artist);
    }

    public Concert readConcert(Artist artist, Concert defaultConcert) {
        var location = promptString(
            "Concert location [%s]: ".formatted(defaultConcert.location()),
            defaultConcert.location()
        );
        var date = promptDate("Concert date [%s]: ".formatted(defaultConcert.date()), defaultConcert.date());
        return new Concert(location, date, artist);
    }

    public Review readReview() {
        int rating;
        do {
            rating = promptInt("Rating: ");
        } while (!(1 <= rating && rating <= 5));

        var text = promptString("Review: ");
        return new Review(rating, text);
    }

    public Genre chooseGenre() {
        return choose(List.of(Genre.values()));
    }

    public List<Track> chooseMultipleTracks(List<Track> tracks) {
        return choose(tracks, true, Track::toLongString);
    }

    public <T> T choose(List<T> options) {
        return choose(options, false, T::toString).get(0);
    }

    public <T> List<T> choose(List<T> options, boolean allowMultiple, Function<T, String> show) {
        // TODO: What if options is empty?
        int id = 1;
        for (var artist : options) {
            System.out.println(id + ". " + show.apply(artist));
            id++;
        }

        var chosenOption = new ArrayList<T>();
        while (chosenOption.isEmpty()) {
            List<Integer> choices;
            if (allowMultiple) {
                choices = promptInts("Choose: ");
            } else {
                choices = List.of(promptInt("Choose: "));
            }

            for (int choice : choices) {
                if (!(1 <= choice && choice <= options.size())) {
                    chosenOption.clear();
                    break;
                }
                chosenOption.add(options.get(choice - 1));
            }
        }

        return chosenOption;
    }
}
