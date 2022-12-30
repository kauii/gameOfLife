package Board;

/*
 * FACADE DESIGN PATTERN
 * Each Board manipulation is handled through the Board class
 *
 * COMMAND DESIGN PATTERN
 * Invoker
 */

import Board.Grid.Evolution;
import Board.Grid.Grid;

import java.util.Stack;

public class Board implements BoardInter {
    private final Grid grid;
    private final Evolution evo;
    //private final Exporter expo;
    private Stack<Object> MemStack;
    private PlayerNr[][] expoBoard;


    public Board(int dimension) {
        grid = new Grid(this, dimension);
        //captureState();
        evo = new Evolution();
        //expo = new Exporter();
        MemStack = new Stack<>();

        // Create export Board
        //expoBoard = expo.gridExport(grid.getGrid(this));
    }

    // Exports board as 2D-PlayerNr array.
    // DEAD, PLAYER1, PLAYER2
    public PlayerNr[][] getBoard() {
        return grid.getGrid(this);
    }

    public PlayerNr[][] setCell(int x_cor, int y_cor, PlayerNr playerNr) {
        // Save the current cell for undo
        captureCell(x_cor, y_cor);
        grid.setCell(this, x_cor, y_cor, playerNr);

        return expoBoard;
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
     * Saves the previous states.
     */

    // Saving the current state of a cell as a memento in the stack
    private void captureCell(int x_cor, int y_cor) {
        MemStack.push(grid.getMemento(x_cor, y_cor));
    }

    public PlayerNr[][] undo() {
        grid.restore(MemStack.pop());

        return grid.getGrid(this);
    }

    private void clearStack() {
        MemStack = new Stack<>();
    }

}
