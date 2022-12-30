package Board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        int dim = 5;
        board = new Board(dim);

        // Setup some initial cells
        board.setCell(0, 0, Cell.PLAYER1);
        board.setCell(0, 1, Cell.PLAYER1);
        board.setCell(1, 0, Cell.PLAYER2);
    }

    @Test
    void getBoard() {
        Cell[][] checkGrid =
                {
                        {Cell.PLAYER1, Cell.PLAYER1, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.PLAYER2, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD}
                };

        assertArrayEquals(checkGrid, board.getBoard());
    }

    @Test
    void evolve() {
        // Create Overpopulation
        board.setCell(2, 3, Cell.PLAYER2);
        board.setCell(3, 2, Cell.PLAYER2);
        board.setCell(3, 3, Cell.PLAYER1);
        board.setCell(3, 4, Cell.PLAYER2);
        board.setCell(4, 3, Cell.PLAYER2);

        Cell[][] checkGrid =
                {
                        {Cell.PLAYER1, Cell.PLAYER1, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.PLAYER2, Cell.PLAYER1, Cell.PLAYER1, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.PLAYER2, Cell.PLAYER1, Cell.PLAYER2},
                        {Cell.DEAD, Cell.DEAD, Cell.PLAYER2, Cell.DEAD, Cell.DEAD}
                };
        board.evolve();
        assertArrayEquals(checkGrid, board.getBoard());
    }

    @Test
    void getPlayerCells() {
        int[] checkCells = {2, 1};
        assertArrayEquals(checkCells, board.getPlayerCells());
    }

    @Test
    void undo() {
        Cell[][] checkGrid;
        // Clear stack manually
        board.clearStack();

        // Change cells
        board.setCell(4, 4, Cell.PLAYER1);
        board.setCell(0, 1, Cell.DEAD);

        // Check grid after setting cells
        checkGrid = new Cell[][]
                {
                        {Cell.PLAYER1, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.PLAYER2, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.PLAYER1}
                };

        assertArrayEquals(checkGrid, board.getBoard());

        // Undo 1
        board.undo();
        checkGrid = new Cell[][]
                {
                        {Cell.PLAYER1, Cell.PLAYER1, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.PLAYER2, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.PLAYER1}
                };

        assertArrayEquals(checkGrid, board.getBoard());

        // Undo 2
        board.undo();
        checkGrid = new Cell[][]
                {
                        {Cell.PLAYER1, Cell.PLAYER1, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.PLAYER2, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD}
                };

        assertArrayEquals(checkGrid, board.getBoard());

        // Undo 3 - invalid
        // Undo 2
        board.undo();
        checkGrid = new Cell[][]
                {
                        {Cell.PLAYER1, Cell.PLAYER1, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.PLAYER2, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD},
                        {Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD, Cell.DEAD}
                };

        assertArrayEquals(checkGrid, board.getBoard());
    }

}