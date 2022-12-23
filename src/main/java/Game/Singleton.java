package Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Singleton {
    private static Singleton instance;
    private final List<Player> players;

    private Singleton() {
        players = new ArrayList<>();
    }

    public static Singleton getInstance() {

        // lazy initialization
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void addToList(Player player) {
        players.add(player);
    }

    public void removeFromList(int index) {
        players.remove(index);
    }

    public void sortList() {
        players.sort(Comparator.comparing(Player::getName));
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public List<Player> getList() {
        return players;
    }
}
