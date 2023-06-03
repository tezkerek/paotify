package menu;

import entities.*;
import repository.AlbumService;
import repository.ArtistService;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Menu {
    Prompter prompter;
    Repository repository;

    public Menu(Repository repository) {
        this.prompter = new Prompter(System.in, System.out);
        this.repository = repository;
    }

    public void run() {
        while (readCommand()) ;
    }

    protected boolean readCommand() {
        var cmd = prompter.promptString("Cmd: ").trim();
        switch (cmd) {
            case "quit", "exit" -> {
                return false;
            }
            case "artist-ls" -> {
                System.out.println(repository.getArtists());
            }
            case "artist-add" -> {
                var artist = readArtist();
                repository.addArtist(artist);
            }
            case "artist-rm" -> {
                var artist = chooseArtist();
                repository.deleteArtist(artist);
                System.out.println("Removed " + artist);
            }
            case "album-ls" -> {
                System.out.println(repository.getAlbums());
            }
            case "album-add" -> {
                var artist = chooseArtist();
                var album = readAlbum(artist);

                var trackCount = prompter.promptInt("Track count: ");
                for (int i = 1; i <= trackCount; i++) {
                    try {
                        album.addTrack(readTrack(i, album));
                    } catch (PromptException e) {
                        System.out.println(e.getMessage());
                        i--;
                    }
                }

                artist.addAlbum(album);
                System.out.printf("Added %s to %s's discography%n", album, artist);
            }
            case "album-rm" -> {
                var artist = chooseArtist();
                var album = chooseAlbum(artist);
                repository.deleteAlbum(artist, album);
                System.out.println("Removed " + album);
            }
            case "album-show" -> {
                var album = chooseAlbum();
                System.out.println(album);
                System.out.println("Artist: " + album.getArtist().getName());
                System.out.println("Genre: " + album.getGenre());
                System.out.println("Rating: " + album.getRating().map(r -> String.format("%.1f", r)).orElse("N/A"));
                System.out.println("Tracklist:");
                album.getTracks().forEach(track -> System.out.printf("#%d. %s\n", track.getNumber(), track.getTitle()));
            }
            case "review-add" -> {
                var album = chooseAlbum();
                var review = readReview();
                album.addReview(review);
            }
            case "album-reviews" -> {
                var reviews = chooseAlbum().getReviews();
                if (reviews.isEmpty()) {
                    System.out.println("No reviews");
                    break;
                }
                reviews.forEach(review -> {
                    var rating = review.rating();
                    var stars = "★".repeat(rating) + "☆".repeat(5 - rating);
                    System.out.println(stars + " " + review.text());
                });
            }
            case "lyrics" -> {
                var track = chooseTrack();
                var lyrics = track.getLyrics();
                System.out.println(lyrics.isBlank() ? "No lyrics available" : lyrics);
            }
            case "lyrics-set" -> {
                var track = chooseTrack();
                var lyrics = new StringBuilder();

                while (true) {
                    var line = prompter.promptString("Verse (empty line to finish): ");
                    if (line.isBlank()) break;
                    lyrics.append(line).append('\n');
                }

                track.setLyrics(lyrics.toString());
            }
            case "playlist-add" -> {
                var playlist = readPlaylist();
                repository.addPlaylist(playlist);
            }
            case "playlist-ls" -> {
                System.out.println(repository.getPlaylists());
            }
            case "playlist-show" -> {
                var playlist = choosePlaylist();
                playlist.getTracks().forEach(track -> System.out.println(track.toLongString()));
            }
            case "concert-add" -> {
                var artist = chooseArtist();
                var concert = readConcert(artist);
                artist.addConcert(concert);
            }
            case "playlist-concerts" -> {
                var playlist = choosePlaylist();
                playlist.getSortedConcerts().forEach(concert -> {
                    var artist = concert.artist();
                    System.out.printf("%s %s - %s\n", concert.date(), concert.location(), artist.getName());
                });
            }
            case "db-artist-ls" -> {
                System.out.println(ArtistService.getInstance().getArtists());
            }
            case "db-artist-add" -> {
                var artist = readArtist();
                ArtistService.getInstance().createArtist(artist);
            }
            case "db-artist-update" -> {
                var artist = chooseDbArtist();
                var newArtist = readArtist(artist);
                ArtistService.getInstance().updateArtist(artist.getId(), newArtist);
            }
            case "db-artist-rm" -> {
                var artist = chooseDbArtist();
                ArtistService.getInstance().deleteArtist(artist.getId());
                System.out.println("Removed " + artist);
            }
            case "db-album-ls" -> {
                System.out.println(AlbumService.getInstance().getAlbums());
            }
            case "db-album-add" -> {
                var album = readAlbum(chooseDbArtist());
                AlbumService.getInstance().createAlbum(album);
            }
            case "db-album-update" -> {
                var album = chooseDbAlbum();
                var newArtist = chooseDbArtist();
                var newAlbum = readAlbum(newArtist, album);
                AlbumService.getInstance().updateAlbum(album.getId(), newAlbum);
            }
            case "db-album-rm" -> {
                var album = chooseDbAlbum();
                AlbumService.getInstance().deleteAlbum(album.getId());
                System.out.println("Removed " + album);
            }
        }

        return true;
    }

    protected Artist readArtist() {
        var name = prompter.promptString("Artist name: ");
        return new Artist(name);
    }

    protected Artist readArtist(Artist defaultArtist) {
        var name = prompter.promptString(
            String.format("Artist name [%s]: ", defaultArtist.getName()),
            defaultArtist.getName()
        );
        return new Artist(name);
    }

    protected Album readAlbum(Artist artist) {
        var title = prompter.promptString("Album title: ");
        var releaseDate = prompter.promptDate("Album release date: ");
        var genre = chooseGenre();

        return new Album(title, releaseDate, genre, artist);
    }

    protected Album readAlbum(Artist artist, Album defaultAlbum) {
        var title = prompter.promptString(
            String.format("Album title[%s]: ", defaultAlbum.getTitle()),
            defaultAlbum.getTitle()
        );
        var releaseDate = prompter.promptDate(
            String.format("Album release date[%s]: ", defaultAlbum.getReleaseDate()),
            defaultAlbum.getReleaseDate()
        );
        var genre = chooseGenre();

        return new Album(title, releaseDate, genre, artist);
    }

    protected Track readTrack(int number, Album album) throws PromptException {
        var title = prompter.promptString(String.format("Track #%d title: ", number));
        var duration = prompter.promptDuration(String.format("Track #%d duration: ", number));

        return new Track(number, title, duration, album);
    }

    protected Playlist readPlaylist() {
        var name = prompter.promptString("Playlist name: ");
        var tracks = chooseMultipleTracks();
        return new Playlist(name, tracks);
    }

    private Concert readConcert(Artist artist) {
        var location = prompter.promptString("Concert location: ");
        var date = prompter.promptDate("Concert date: ");
        return new Concert(location, date, artist);
    }

    protected Review readReview() {
        int rating;
        do {
            rating = prompter.promptInt("Rating: ");
        } while (!(1 <= rating && rating <= 5));

        var text = prompter.promptString("Review: ");
        return new Review(rating, text);
    }

    protected Genre chooseGenre() {
        return choose(List.of(Genre.values()));
    }

    protected Artist chooseArtist() {
        return choose(repository.getArtists());
    }

    protected Artist chooseDbArtist() {
        return choose(ArtistService.getInstance().getArtists());
    }

    protected Album chooseAlbum(Artist artist) {
        return choose(artist.getAlbums());
    }

    protected Album chooseDbAlbum() {
        return choose(AlbumService.getInstance().getAlbums());
    }

    protected Album chooseAlbum() {
        return chooseAlbum(chooseArtist());
    }

    protected Track chooseTrack(Album album) {
        return choose(album.getTracks());
    }

    protected Track chooseTrack() {
        return chooseTrack(chooseAlbum());
    }

    protected Playlist choosePlaylist() {
        return choose(repository.getPlaylists());
    }

    protected List<Track> chooseMultipleTracks() {
        return choose(repository.getTracks(), true, Track::toLongString);
    }

    protected <T> T choose(List<T> options) {
        return choose(options, false, T::toString).get(0);
    }

    protected <T> List<T> choose(List<T> options, boolean allowMultiple, Function<T, String> show) {
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
                choices = prompter.promptInts("Choose: ");
            } else {
                choices = List.of(prompter.promptInt("Choose: "));
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
