package menu;

import entities.*;
import repository.*;

public class Menu {
    EntityPrompter prompter;
    Repository repository;

    public Menu(Repository repository) {
        this.prompter = new EntityPrompter(System.in, System.out);
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
                var artist = prompter.readArtist();
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
                var album = prompter.readAlbum(artist);

                var trackCount = prompter.promptInt("Track count: ");
                for (int i = 1; i <= trackCount; i++) {
                    try {
                        album.addTrack(prompter.readTrack(i, album));
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
                var review = prompter.readReview();
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
                var concert = prompter.readConcert(artist);
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
                var artist = prompter.readArtist();
                ArtistService.getInstance().createArtist(artist);
            }
            case "db-artist-update" -> {
                var artist = chooseDbArtist();
                var newArtist = prompter.readArtist(artist);
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
                var album = prompter.readAlbum(chooseDbArtist());
                AlbumService.getInstance().createAlbum(album);
            }
            case "db-album-update" -> {
                var album = chooseDbAlbum();
                var newArtist = chooseDbArtist();
                var newAlbum = prompter.readAlbum(newArtist, album);
                AlbumService.getInstance().updateAlbum(album.getId(), newAlbum);
            }
            case "db-album-rm" -> {
                var album = chooseDbAlbum();
                AlbumService.getInstance().deleteAlbum(album.getId());
                System.out.println("Removed " + album);
            }
            case "db-track-ls" -> {
                System.out.println(TrackService.getInstance().getAllTracks());
            }
            case "db-track-add" -> {
                var album = chooseDbAlbum();
                var number = album.getTrackCount() + 1;
                var track = prompter.readTrack(number, album);
                TrackService.getInstance().createTrack(track);
            }
            case "db-track-update" -> {
                var track = chooseDbTrack();
                var newTrack = prompter.readTrack(track.getNumber(), track.getAlbum(), track);
                TrackService.getInstance().updateTrack(track.getId(), newTrack);
            }
            case "db-track-rm" -> {
                var track = chooseDbTrack();
                TrackService.getInstance().deleteTrack(track.getId());
                System.out.println("Removed " + track);
            }
            case "db-concert-ls" -> System.out.println(ConcertService.getInstance().getConcerts());
            case "db-concert-add" -> {
                var concert = prompter.readConcert(chooseDbArtist());
                ConcertService.getInstance().createConcert(concert);
            }
            case "db-concert-update" -> {
                var concert = chooseDbConcert();
                var newConcert = prompter.readConcert(concert.artist(), concert);
                ConcertService.getInstance().updateConcert(concert.getId(), newConcert);
            }
            case "db-concert-rm" -> {
                var concert = chooseDbConcert();
                ConcertService.getInstance().deleteConcert(concert.getId());
                System.out.println("Removed " + concert);
            }
        }

        return true;
    }

    protected Playlist readPlaylist() {
        return prompter.readPlaylist(repository.getTracks());
    }

    protected Artist chooseArtist() {
        return prompter.choose(repository.getArtists());
    }

    protected Artist chooseDbArtist() {
        return prompter.choose(ArtistService.getInstance().getArtists());
    }

    protected Album chooseAlbum(Artist artist) {
        return prompter.choose(artist.getAlbums());
    }

    protected Album chooseDbAlbum(Artist artist) {
        return prompter.choose(AlbumService.getInstance().getAlbumsByArtist(artist.getId()));
    }

    protected Album chooseDbAlbum() {
        return chooseDbAlbum(chooseDbArtist());
    }

    protected Album chooseAlbum() {
        return chooseAlbum(chooseArtist());
    }

    protected Track chooseTrack(Album album) {
        return prompter.choose(album.getTracks());
    }

    protected Track chooseTrack() {
        return chooseTrack(chooseAlbum());
    }

    protected Track chooseDbTrack(Album album) {
        return prompter.choose(TrackService.getInstance().getTracksByAlbum(album.getId()));
    }

    protected Track chooseDbTrack() {
        return chooseDbTrack(chooseDbAlbum());
    }

    protected Playlist choosePlaylist() {
        return prompter.choose(repository.getPlaylists());
    }

    protected Concert chooseDbConcert() {
        return prompter.choose(ConcertService.getInstance().getConcerts());
    }
}
