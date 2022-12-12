package Board;

import java.util.BitSet;

/*
 * Evolution class to progress to the next evolution
 * Methods can only be called by Board
 * Iterating through a copy of the grid and setting new cells directly in the grid
 */

public class Evolution {

    // Can only be called by Board
    protected void evolve(Object o, Grid grid) {
        // Check if called by Board -> otherwise return
        if (!(o instanceof Board)) {
            return;
        }
        // Create copy of grid for iteration
        BitSet[][] old_grid = copyGrid(grid.getGrid(this));
        //BitSet[][] old_grid = grid.getGrid(this).clone();
        int dim = old_grid.length;
        int[] neighbours;

        // Iterate through old_grid[x][y]
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                neighbours = getNeighbours(old_grid, x, y);
                // If alive (index 0 == 1)
                if (old_grid[x][y].get(0)) {
                    // If less than 2 or more than 3 neighbours -> kill cell
                    if (neighbours[0] < 2 || neighbours[0] > 3) {
                        grid.setCell(this, x, y, false, false);
                    }
                    // else -> do nothing
                } else {
                    // If neighbours == 3 -> create new cell
                    if (neighbours[0] == 3) {
                        // Create cell for player with most cells nearby
                        grid.setCell(this, x, y, true, neighbours[1] != 0);
                    }
                }
            }
        }
    }

    // Creates a copy of the grid as not to interfere with manipulations
    private BitSet[][] copyGrid(BitSet[][] grid){
        int dim=grid.length;
        BitSet[][] res=new BitSet[dim][dim];
        for(int i=0;i<dim;i++){
            for(int j=0;j<dim;j++){
                res[i][j]= (BitSet) grid[i][j].clone();
            }
        }

        return res;
    }

    // Get the number of neighbours and the player with the majority of cells
    private int[] getNeighbours(BitSet[][] old_grid, int x_cor, int y_cor) {
        int[] res = new int[2];       // [number of neighbours, player most cells]
        // Cell counter
        int neighbours = 0;
        // Player counter
        int player_1 = 0;
        int player_2 = 0;
        // Cell status
        int status;

        // Iterate through all eight neighbouring cells
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Checking if not cell itself (i==j==0)
                if (!(i == 0 && j == 0)) {
                    status = getCellStatus(old_grid, x_cor + i, y_cor + j);
                    // Check if cell alive and act accordingly
                    if (status == 1) {
                        neighbours++;
                        player_1++;
                    } else if (status == 2) {
                        neighbours++;
                        player_2++;
                    }
                }
            }
        }

        // Set values in res
        res[0] = neighbours;
        if (player_1 < player_2) {
            res[1] = 1;           // else: already set to 0
        }
        return res;
    }

    // Get the status and player of a specific cell (0=dead, 1=player_1, 2=player_2
    private int getCellStatus(BitSet[][] old_grid, int x_cor, int y_cor) {
        // Try-catch
        try {
            // If cell not alive
            if (!old_grid[x_cor][y_cor].get(0)) {
                return 0;
            } else {
                // If player !true -> player 1
                if (!old_grid[x_cor][y_cor].get(1)) {
                    return 1;
                } else {
                    return 2;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }

    }

}
