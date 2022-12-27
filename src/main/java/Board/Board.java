package Board;

/*
 * FACADE DESIGN PATTERN
 * Each Board manipulation is handled through the Board class
 */

import Game.PlayerNr;

import java.util.Stack;

public class Board implements BoardInter {
    private final Grid grid;
    private final Evolution evo;
    private final Exporter expo;
    private Stack<Object> stack;


    public Board(int dimension) {
        grid = new Grid(this, dimension);
        // Save empty grid as origin
        stack=new Stack<>();
        captureState();
        evo = new Evolution();
        expo = new Exporter();
    }

    // Exports board as 2D-short array.
    // 0 || 1 = cell is dead
    // 2 = alive, player 1
    // 3 = alive, player 2
    public short[][] getBoard() {
        return expo.gridExport(grid.getGrid(this));
    }

    public short[][] setCell(int x_cor, int y_cor, boolean alive, PlayerNr playerNr) {
        // If playerNr == 1 -> player = false
        // If playerNr == 2 -> player = true
        boolean player = playerNr == PlayerNr.PLAYER2;
        grid.setCell(this, x_cor, y_cor, alive, player);

        return expo.gridExport(grid.getGrid(this));
    }

    public void evolve() {
        // Grid progresses one evolution
        evo.evolve(this, grid);

        // Reset Stack
        resetStack();

        // Save current state as origin
        captureState();
    }

    // Get int array [player_1,player_2]
    public int[] getPlayerCells() {
        return grid.getPlayerCells();
    }


    /*
     * MEMENTO DESIGN PATTERN
     * Caretaker function.
     * Saves the previous states and allows undo.
     */
    // Saving the current state as a memento in the stack
    private void captureState() {
        stack.push(grid.getMemento());
    }

    // Resets stack at the end of each round
    private void resetStack() {
        stack = new Stack<>();
    }

    // Undo last step by reloading previous step and returning it as short[][]
    public short[][] undo() {
        // Will only restore if stack not empty
        if (!stack.isEmpty()) {
            grid.restore(stack.pop());
        }
        return expo.gridExport(grid.getGrid(this));
    }

}
