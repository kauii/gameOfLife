package Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Objects;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Alice", Color.GREEN);
    }

    @Test
    void getName() {
        assert (Objects.equals(player.getName(), "Alice"));
    }

    @Test
    void getColor() {
        assert (player.getColor() == Color.GREEN);
    }

    @Test
    void getSetPlayerNr() {
        player.setPlayerNr(PlayerNr.PLAYER2);
        assert (player.getPlayerNr() == PlayerNr.PLAYER2);
    }

    @Test
    void getLiveCells() {
        assert (player.getLiveCells() == 0);
    }

    @Test
    void setLiveCells() {
        player.setLiveCells(5);
        assert (player.getLiveCells() == 5);
        player.setLiveCells(13);
        assert (player.getLiveCells() == 13);
    }
}