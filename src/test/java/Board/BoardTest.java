package Board;

import Game.PlayerNr;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/*
 * Entire Board Package tested through Board
 */

class BoardTest {
    Board board;
    private final int dim = 10;

    @BeforeEach
    void setUp() {
        board = new Board(dim);

        // Cells setup
        board.setCell(0, 0, true, PlayerNr.PLAYER1);
        board.setCell(0, 1, true, PlayerNr.PLAYER1);
        board.setCell(1, 0, true, PlayerNr.PLAYER2);
    }

    @org.junit.jupiter.api.Test
    void getBoard() {
        // Set 3,3 to Player 2
        board.setCell(3, 3, false, PlayerNr.PLAYER2);
        // Create initial save through one evolution
        board.evolve();

        // Check grid
        short[][] checkGrid =
                {
                        {2, 2, 0, 0, 0, 0, 0, 0, 0, 0},
                        {3, 2, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                };
        //printArray(board.getBoard());
        assertArrayEquals(checkGrid, board.getBoard());

    }

    @org.junit.jupiter.api.Test
    void setCell() {
        // Tested in getBoard() Test
    }

    @org.junit.jupiter.api.Test
    void getDimension() {
        assert (board.getDimension() == dim);
    }

    @org.junit.jupiter.api.Test
    void evolve() {
        // 1,1 -> 2 created;
        // 3,3 & 7,7 & 8,8 -> set 0
        // Check that over- and underpopulated cells are deleted
        // Set 2,2 to Player 2 ALIVE
        board.setCell(3, 3, true, PlayerNr.PLAYER2);
        // Set 9,9 to Player 2 ALIVE
        board.setCell(9, 9, true, PlayerNr.PLAYER2);
        // Set 9,8 to Player 2 ALIVE
        board.setCell(9, 8, true, PlayerNr.PLAYER2);
        // Set 8,9 to Player 2 ALIVE
        board.setCell(8, 9, true, PlayerNr.PLAYER2);
        // Set 8,8 to Player 2 ALIVE
        board.setCell(8, 8, true, PlayerNr.PLAYER2);
        // Set 7,7 to Player 2 ALIVE
        board.setCell(7, 7, true, PlayerNr.PLAYER2);

        // Create initial save through one evolution
        board.evolve();

        // Check grid
        short[][] checkGrid =
                {
                        {2, 2, 0, 0, 0, 0, 0, 0, 0, 0},
                        {3, 2, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 3, 0},
                        {0, 0, 0, 0, 0, 0, 0, 3, 0, 3},
                        {0, 0, 0, 0, 0, 0, 0, 0, 3, 3},
                };
        //printArray(board.getBoard());
        assertArrayEquals(checkGrid, board.getBoard());
    }

    @org.junit.jupiter.api.Test
    void getHistory() {
        board.evolve();
        board.evolve();
        board.evolve();
        assert (board.getHistory().size()==3);
    }

    @org.junit.jupiter.api.Test
    void getPlayerCells() {
        int[] cells={2,1};
        assertArrayEquals(cells, board.getPlayerCells());
    }
}