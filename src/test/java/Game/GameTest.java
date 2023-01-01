package Game;

import Board.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Objects;

class GameTest {
    private Game game;
    private Singleton players;
    @BeforeEach
    void setUp() {
        players = Singleton.getInstance();

        // players only added in this test class
        try {
            players.addToList(new Player("Player 2", Color.RED));
        } catch (IllegalCallerException ignored) {

        }
        try {
            players.addToList(new Player("Player 1", Color.BLUE));
        } catch (IllegalCallerException ignored) {

        }

        game = new Game();
        int dimension = 10;
        game.setUp(dimension);
    }

    @Test
    void sortList() {
        assert(Objects.equals(players.getPlayer(0).getName(), "Player 1"));
        assert(Objects.equals(players.getPlayer(1).getName(), "Player 2"));
    }

    @Test
    void skipGen() {
        game.updateCell(0,0, Cell.PLAYER1);
        game.updateCell(0,1,Cell.PLAYER1);
        game.updateCell(1,1, Cell.PLAYER2);
        Cell[][] boardBefore = game.getBoard();

        game.skipGen();
        assert(boardBefore != game.getBoard());
    }

    @Test
    void reset() {
        game.updateCell(0,0, Cell.PLAYER1);
        game.updateCell(1,1, Cell.PLAYER2);

        game.reset();

        assert(game.getBoard()[0][0] == Cell.DEAD);
        assert(game.getBoard()[1][1] == Cell.DEAD);
    }

    @Test
    void updateCell() {
        game.updateCell(0,0, Cell.PLAYER2);
        Assertions.assertEquals(game.getBoard()[0][0], Cell.PLAYER2);
        assert (players.getPlayer(1).getLiveCells() == 1);
        assert (players.getPlayer(0).getLiveCells() == 0);
    }
}