package Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Singleton {
    private static final Singleton instance = new Singleton();
    private final List<Player> players;

    private Singleton() {
        players = new ArrayList<>();
    }

    public static Singleton getInstance() {
        return instance;
    }

    public void addToList(Player player) {
        if (players.size() < 2) { players.add(player); }
        else {
            throw new IllegalCallerException("Cannot add more than 2 players to the list");
        }
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
