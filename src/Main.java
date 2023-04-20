import entities.*;
import menu.Menu;
import repository.InMemoryRepository;
import repository.Repository;

import java.time.Duration;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("PAOTIFY v0.1.0");

        Repository repo = new InMemoryRepository();
        seed(repo);

        var menu = new Menu(repo);
        menu.run();
    }

    public static void seed(Repository repo) {
        var sp = new Artist("Silent Planet");

        var iridescent = new Album("Iridescent", LocalDate.now(), Genre.METAL, sp);
        iridescent.addTrack(new Track(2, "Translate the Night", Duration.ofMinutes(3), iridescent));
        iridescent.addTrack(new Track(3, "Trilogy", Duration.ofMinutes(3).plusSeconds(23), iridescent));
        iridescent.addTrack(new Track(6, "Panopticon", Duration.ofMinutes(3).plusSeconds(34), iridescent));
        sp.addAlbum(iridescent);

        repo.addArtist(sp);

        var bo = new Artist("Bad Omens");

        var tdpm = new Album("THE DEATH OF PEACE OF MIND", LocalDate.now(), Genre.METAL, bo);
        tdpm.addTrack(new Track(1, "CONCRETE JUNGLE", Duration.ofMinutes(3).plusSeconds(40), tdpm));
        tdpm.addTrack(new Track(2, "Nowhere to go", Duration.ofMinutes(4).plusSeconds(6), tdpm));
        bo.addAlbum(tdpm);

        var fgbgfm = new Album("Finding God Before God Finds Me", LocalDate.now(), Genre.METAL, bo);
        tdpm.addTrack(new Track(1, "Kingdom Of Cards", Duration.ofMinutes(4).plusSeconds(21), tdpm));
        tdpm.addTrack(new Track(2, "Running in Circles", Duration.ofMinutes(3).plusSeconds(56), tdpm));
        tdpm.addTrack(new Track(3, "Careful What You Wish For", Duration.ofMinutes(4).plusSeconds(32), tdpm));
        bo.addAlbum(fgbgfm);

        repo.addArtist(bo);

        repo.addPlaylist(new Playlist("Fire mixtape", iridescent.getTracks()));
    }
}