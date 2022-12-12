package Board;

import java.util.BitSet;

/*
 * Grid shall only be created by Board
 * Memento can access the get methods, Board the set methods
 * Evolution can access the getGrid and setCell methods
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
        // Call has to be made by Board or Evolution, otherwise no action
        if (o instanceof Board || o instanceof Evolution) {

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
        // If call not made by Memento or Evolution, null return
        if (!(o instanceof Memento || o instanceof Evolution)) {
            return null;
        }
        return grid;
    }

    // Get int array [player_1,player_2]
    protected int[] getPlayerCells() {
        int p1 = 0;
        int p2 = 0;

        // Iterate through grid
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                // If cell alive
                if (grid[i][j].get(0)) {
                    // Add cell count to player
                    if (!grid[i][j].get(1)) {
                        p1++;
                    } else {
                        p2++;
                    }
                }
            }
        }
        return new int[]{p1, p2};
    }

}
