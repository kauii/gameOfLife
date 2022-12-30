package Board;

/*
 * FACADE DESIGN PATTERN
 * Each Board manipulation is handled through the Board class
 */

import Board.Grid.Evolution;
import Board.Grid.Grid;

import java.util.Stack;

public class Board implements BoardInter {
    private final Grid grid;
    private final Evolution evo;
    private Stack<Object> MemStack;


    public Board(int dimension) {
        grid = new Grid(this, dimension);
        //captureState();
        evo = new Evolution();
        MemStack = new Stack<>();

    }

    // Exports board as 2D-PlayerNr array.
    // DEAD, PLAYER1, PLAYER2
    public Cell[][] getBoard() {
        return grid.getGrid(this);
    }

    public Cell[][] setCell(int x_cor, int y_cor, Cell cell) {
        // Save the current cell for undo
        captureCell(x_cor, y_cor);
        grid.setCell(this, x_cor, y_cor, cell);

        return grid.getGrid(this);
    }

    public void evolve() {
        // Grid progresses one evolution
        evo.evolve(this, grid);

        // Reset MemStack
        clearStack();
    }

    // Get int array [player_1,player_2]
    public int[] getPlayerCells() {
        return grid.getPlayerCells();
    }


    /*
     * MEMENTO DESIGN PATTERN
     * Caretaker function.
     * Saves the previous state from changed cells.
     */

    // Saving the current state of a cell as a memento in the stack
    private void captureCell(int x_cor, int y_cor) {
        MemStack.push(grid.getMemento(x_cor, y_cor));
    }

    public Cell[][] undo() {
        try {
            grid.restore(MemStack.pop());
        } catch (Exception ignored) {
        }
        return grid.getGrid(this);
    }

    public void clearStack() {
        MemStack = new Stack<>();
    }

}
