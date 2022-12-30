package Board.Grid;

import Board.Board;
import Board.PlayerNr;

/*
 * Evolution class to progress to the next evolution
 * Methods can only be called by Board
 * Iterating through a copy of the grid and setting new cells directly in the grid
 */

public class Evolution {

    // Can only be called by Board
    public void evolve(Object o, Grid grid) {
        // Check if called by Board -> otherwise return
        if (!(o instanceof Board)) {
            return;
        }
        // Create copy of grid for iteration
        PlayerNr[][] old_grid = grid.getGrid(this).clone(); //copyGrid(grid.getGrid(this));
        //BitSet[][] old_grid = grid.getGrid(this).clone();
        int dim = old_grid.length;
        int[] neighbours;

        // Iterate through old_grid[x][y]
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                neighbours = getNeighbours(old_grid, x, y);
                // If alive (index 0 == 1)
                if (old_grid[x][y]!=PlayerNr.DEAD) {
                    // If less than 2 or more than 3 neighbours -> kill cell
                    if (neighbours[0] < 2 || neighbours[0] > 3) {
                        grid.setCell(this, x, y, PlayerNr.DEAD);
                    }
                    // else -> do nothing
                } else {
                    // If neighbours == 3 -> create new cell
                    if (neighbours[0] == 3) {
                        // Create cell for player with most cells nearby
                        if(neighbours[1]==0){
                            grid.setCell(this,x,y,PlayerNr.PLAYER1);
                        }else{
                            grid.setCell(this,x,y,PlayerNr.PLAYER2);
                        }
                    }
                }
            }
        }
    }

    // Get the number of neighbours and the player with the majority of cells
    private int[] getNeighbours(PlayerNr[][] old_grid, int x_cor, int y_cor) {
        int[] res = new int[2];       // [number of neighbours, player most cells]
        // Cell counter
        int neighbours = 0;
        // Player counter
        int player_1 = 0;
        int player_2 = 0;
        // Cell status
        PlayerNr status;

        // Iterate through all eight neighbouring cells
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Checking if not cell itself (i==j==0)
                if (!(i == 0 && j == 0)) {
                    status = getCellStatus(old_grid, x_cor + i, y_cor + j);
                    // Check if cell alive and act accordingly
                    if (status == PlayerNr.PLAYER1) {
                        neighbours++;
                        player_1++;
                    } else if (status == PlayerNr.PLAYER2) {
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
    private PlayerNr getCellStatus(PlayerNr[][] old_grid, int x_cor, int y_cor) {
        // Try-catch
        try {
            return old_grid[x_cor][y_cor];
        } catch (ArrayIndexOutOfBoundsException e) {
            return PlayerNr.DEAD;
        }

    }

}
