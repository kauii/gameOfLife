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

class Grid {
    private final Cell[][] grid;
    private final int dim;

    // Constructor, creates the grid
    Grid(Object o, int dimension) {
        if (!(o instanceof Board)) {
            throw new IllegalCallerException("Only Board can create a new Grid!!!");
        }
        dim = dimension;
        // Create 2D-BitSet array
        grid = new Cell[dimension][dimension];
        // Initialized all as DEAD
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                grid[i][j] = Cell.DEAD;
            }
        }
    }

    // Set value of a cell to dead or alive + player
    // Arguments: true=1, false=0
    protected void setCell(int x_cor, int y_cor, Cell player) {
        // Set first bit status to alive or dead
        grid[x_cor][y_cor] = player;
    }

    // Returns the grid as BitSet to the Memento
    protected Cell[][] getGrid() {
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
                if (grid[i][j] != Cell.DEAD) {
                    // Check if player 1
                    if (grid[i][j] == Cell.PLAYER1) {
                        p1++;
                    } else {
                        p2++;
                    }
                }
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
