package menu;

import entities.Album;
import entities.Artist;
import entities.Track;
import repository.Repository;

import java.util.List;

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
        }

        return true;
    }

    protected Artist readArtist() {
        var name = prompter.promptString("Artist name: ");
        return new Artist(name);
    }

    protected Album readAlbum(Artist artist) {
        var title = prompter.promptString("Album title: ");
        var releaseDate = prompter.promptDate("Album release date: ");

        return new Album(title, releaseDate, artist);
    }

    protected Track readTrack(int number, Album album) throws PromptException {
        var title = prompter.promptString(String.format("Track #%d title: ", number));
        var duration = prompter.promptDuration(String.format("Track #%d duration: ", number));

        return new Track(number, title, duration, album);
    }

    protected Artist chooseArtist() {
        return choose(repository.getArtists());
    }

    protected Album chooseAlbum(Artist artist) {
        return choose(artist.getAlbums());
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

    protected <T> T choose(List<T> options) {
        // TODO: What if options is empty?
        int id = 1;
        for (var artist : options) {
            System.out.println(id + ". " + artist);
            id++;
        }

        T chosenOption = null;
        while (chosenOption == null) {
            var choice = prompter.promptInt("Choose: ");
            if (!(1 <= choice && choice <= options.size())) continue;

            chosenOption = options.get(choice - 1);
        }

        return chosenOption;
    }
}
