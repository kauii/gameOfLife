package Board;

import java.util.BitSet;

/*
 * Grid shall only be created by Board
 * Memento can access the get methods, Board the set methods
 */

public class Grid {
    private final BitSet[][] grid;
    private final int dim;

    // Constructor, creates the grid
    protected Grid(Object o, int dimension) {
        if (!(o instanceof Board)) {
            throw new IllegalCallerException("Only Board can create a new Grid!!!");
        }
        dim = dimension;
        // Create 2D-BitSet array
        grid = new BitSet[dimension][dimension];
        // Initialized all as 0
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j] = new BitSet(2);
            }
        }
    }

    // Set value of a cell to dead or alive + player
    // Arguments: true=1, false=0
    protected void setCell(Object o, int x_cor, int y_cor, boolean alive, boolean player) {
        // Call has to be made by Board, otherwise no action
        if (o instanceof Board) {

            // Set first bit status to alive or dead
            grid[x_cor][y_cor].set(0, alive);
            // Set second bit to relevant player
            grid[x_cor][y_cor].set(1, player);
        }
    }

    // Returns the dimension of the grid
    protected int getDimension() {
        return dim;
    }

    // Returns the grid as BitSet to the Memento
    protected BitSet[][] getGrid(Object o) {
        // If call not made by Memento, null return
        if (!(o instanceof Memento)) {
            return null;
        }
        return grid;
    }

}
