package GUI;

import Game.Player;

import java.util.List;

public interface Observer {
    void updatePlayers(List<Player> players);
}
