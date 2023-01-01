package Game;

import Board.Cell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

class GameTest {
    private Game game;
    private final Singleton players = Singleton.getInstance();

    @BeforeEach
    void setUp() {
        Player player2 = new Player("Player 2", Color.green);
        Player player1 = new Player("Player 1", Color.red);
        players.addToList(player2);
        players.addToList(player1);
        game = new Game();
        int dimension = 10;
        game.setUp(dimension);
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