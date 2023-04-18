package menu;

import entities.Album;
import entities.Artist;
import entities.Track;
import repository.Repository;

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
        var cmd = prompter.promptString("Cmd: ");
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
        // TODO: What if there are no artists?
        var artists = repository.getArtists();

        int id = 1;
        for (var artist : artists) {
            System.out.println(id + ". " + artist);
            id++;
        }

        Artist chosenArtist = null;
        while (chosenArtist == null) {
            var choice = prompter.promptInt("Choose artist: ");
            if (!(1 <= choice && choice <= artists.size())) continue;

            chosenArtist = artists.get(choice - 1);
        }

        return chosenArtist;
    }
}
