package Board;

/*
 * Evolution class to progress to the next evolution
 * Methods can only be called by Board
 * Iterating through a copy of the grid and setting new cells directly in the grid
 */

import Game.GridIterator;

class Evolution {

    // Can only be called by Board
    protected void evolve(Object o, Grid grid) {
        // Check if called by Board -> otherwise return
        if (!(o instanceof Board)) {
            return;
        }
        // Create copy of grid for iteration
        Cell[][] old_grid = grid.getGridCopy(); //copyGrid(grid.getGrid(this));
        //BitSet[][] old_grid = grid.getGrid(this).clone();
        int[] neighbours;

        // Iterate through old_grid[x][y]
        GridIterator iterator = new GridIterator(old_grid);
        while (iterator.hasNext()) {
            int row = iterator.getRow();
            int col = iterator.getCol();
            Cell cell = iterator.next();
            neighbours = getNeighbours(old_grid, row, col);
            // If alive (index 0 == 1)
            if (cell != Cell.DEAD) {
                if (neighbours[0] < 2 || neighbours[0] > 3) {
                    grid.setCell(row, col, Cell.DEAD);
                }
                // else -> do nothing
            } else {
                // If neighbours == 3 -> create new cell
                if (neighbours[0] == 3) {
                    // Create cell for player with most cells nearby
                    if (neighbours[1] == 0) {
                        grid.setCell(row, col, Cell.PLAYER1);
                    } else {
                        grid.setCell(row, col, Cell.PLAYER2);
                    }
                }
            }
        }
    }

    // Get the number of neighbours and the player with the majority of cells
    private int[] getNeighbours(Cell[][] old_grid, int x_cor, int y_cor) {
        int[] res = new int[2];       // [number of neighbours, player most cells]
        // Cell counter
        int neighbours = 0;
        // Player counter
        int player_1 = 0;
        int player_2 = 0;
        // Cell status
        Cell status;

        // Iterate through all eight neighbouring cells
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Checking if not cell itself (i==j==0)
                if (!(i == 0 && j == 0)) {
                    status = getCellStatus(old_grid, x_cor + i, y_cor + j);
                    // Check if cell alive and act accordingly
                    if (status == Cell.PLAYER1) {
                        neighbours++;
                        player_1++;
                    } else if (status == Cell.PLAYER2) {
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
    private Cell getCellStatus(Cell[][] old_grid, int x_cor, int y_cor) {
        // Try-catch
        try {
            return old_grid[x_cor][y_cor];
        } catch (ArrayIndexOutOfBoundsException e) {
            return Cell.DEAD;
        }

    }

}
