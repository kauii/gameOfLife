package Board;

/*
 * Grid shall only be created by Board
 * Certain methods can only be accessed by Board or Evolution
 *
 * MEMENTO DESIGN PATTERN
 * Originator function.
 * Memento function. -> See below
 *
 * COMMAND DESIGN PATTERN
 * Receiver
 */

import Game.GridIterator;

class Grid {
    private final Cell[][] grid;
    private GridIterator iterator;

    // Constructor, creates the grid
    Grid(Object o, int dimension) {
        if (!(o instanceof Board)) {
            throw new IllegalCallerException("Only Board can create a new Grid!!!");
        }
        // Create 2D-BitSet array
        grid = new Cell[dimension][dimension];
        // Initialized all as DEAD
        iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            int row = iterator.getRow();
            int col = iterator.getCol();
            grid[row][col] = Cell.DEAD;
            iterator.next();
        }
    }

    // Set value of a cell to dead or alive + player
    // Arguments: true=1, false=0
    protected void setCell(int x_cor, int y_cor, Cell player) {
        // Set first bit status to alive or dead
        grid[x_cor][y_cor] = player;
    }

    // Returns the grid as BitSet to the Memento
    protected Cell[][] getGridCopy() {
        Cell[][] res = new Cell[grid.length][grid.length];
        // Create copy of array
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, res[i], 0, grid.length);
        }
        return res;
    }

    // Get int array [player_1,player_2]
    protected int[] getPlayerCells() {
        int p1 = 0;
        int p2 = 0;

        // Iterate through grid
        iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            // Check if player 1
            if (cell == Cell.PLAYER1) {
                p1++;
            }
            if (cell == Cell.PLAYER2) {
                p2++;
            }
        }
        return new int[]{p1, p2};
    }


    /*
     * MEMENTO DESIGN PATTERN
     * Memento function.
     * Creates new Memento and resets Grid as state from Memento if required.
     */

    private class Memento {
        private final int x_cor;
        private final int y_cor;
        private final Cell cell;

        // Constructor - Creates a clone of the current state
        private Memento(int x_cor, int y_cor) {
            this.x_cor = x_cor;
            this.y_cor = y_cor;
            this.cell = grid[x_cor][y_cor];
        }

        private int[] getCoordinates() {
            return new int[]{x_cor, y_cor};
        }

        private Cell getCell() {
            return cell;
        }
    }

    // Creates a new Memento to be saved.
    protected Memento getMemento(int x_cor, int y_cor) {
        return new Memento(x_cor, y_cor);
    }

    // Sets Grid to the state of the Memento
    protected void restore(Object o) {
        Memento m = (Memento) o;
        int[] cor = m.getCoordinates();
        Cell cell = m.getCell();
        setCell(cor[0], cor[1], cell);
    }


}
