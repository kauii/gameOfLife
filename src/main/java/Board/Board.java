package Board;

/*
 *  Facade Design Pattern
 *  Each Board manipulation is handled through the Board class
 *
 *  Memento Design Pattern
 *  After the end of each turn, the state of the Board is saved in the Caretaker
 *  Enables us to show a review of the game at the end
 */

import Game.PlayerNr;

import java.util.ArrayList;

public class Board implements BoardInter {
    private final Grid grid;
    private final Caretaker ct;
    private final Evolution evo;

    public Board(int dimension) {
        grid = new Grid(this, dimension);
        ct = new Caretaker(grid);
        evo = new Evolution();
    }

    // Exports board as 2D-short array.
    // 0 || 1 = cell is dead
    // 2 = alive, player 1
    // 3 = alive, player 2
    public short[][] getBoard() {
        return ct.getCurrent();
    }

    public void setCell(int x_cor, int y_cor, boolean alive, PlayerNr playerNr) {
        // If playerNr == 1 -> player = false
        // If playerNr == 2 -> player = true
        boolean player = playerNr == PlayerNr.PLAYER2;
        grid.setCell(this, x_cor, y_cor, alive, player);
    }

    public int getDimension() {
        return grid.getDimension();
    }

    public void evolve() {
        // Current state gets saved in Memento Design Pattern for game history
        ct.saveState();
        // Grid progresses one evolution
        evo.evolve(this, grid);

    }

    public ArrayList<short[][]> getHistory() {
        return ct.getHistory();
    }

    // Get int array [player_1,player_2]
    public int[] getPlayerCells() {
        return grid.getPlayerCells();
    }

}
