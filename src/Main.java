import menu.Menu;
import repository.InMemoryRepository;
import repository.Repository;

public class Main {
    public static void main(String[] args) {
        System.out.println("PAOTIFY v0.1.0");

        Repository repo = new InMemoryRepository();
        var menu = new Menu(repo);
        menu.run();
    }
}