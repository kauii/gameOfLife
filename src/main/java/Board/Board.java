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

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class Board implements BoardInter {
    private final Grid grid;
    private final Evolution evo;
    private final Exporter expo;
    private Stack<Object> stack;


    public Board(int dimension) {
        grid = new Grid(this, dimension);
        // Save empty grid as origin
        stack = new Stack<>();
        captureState();
        evo = new Evolution();
        expo = new Exporter();
    }

    // Exports board as 2D-PlayerNr array.
    // DEAD, PLAYER1, PLAYER2
    public PlayerNr[][] getBoard() {
        return expo.gridExport(grid.getGrid(this));
    }

    public PlayerNr[][] setCell(int x_cor, int y_cor, boolean alive, PlayerNr playerNr) {
        // If playerNr == 1 -> player = false
        // If playerNr == 2 -> player = true
        boolean player = playerNr == PlayerNr.PLAYER2;
        grid.setCell(this, x_cor, y_cor, alive, player);

        return expo.gridExport(grid.getGrid(this));
    }

    public void evolve() {
        // Grid progresses one evolution
        evo.evolve(this, grid);

        // Save current state in history
        captureState();
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
    // Saving the current state as a memento in the stack
    private void captureState() {
        stack.push(grid.getMemento());
    }

    // Get the history of the entire game as an ArrayList of PlayerNr[][]
    public Stack<PlayerNr[][]> getHistory() {
        Stack<PlayerNr[][]> hist = new Stack<>();
        while (!stack.isEmpty()) {
            grid.restore(stack.pop());
            hist.push(expo.gridExport(grid.getGrid(this)));
        }
        return hist;
    }

}
