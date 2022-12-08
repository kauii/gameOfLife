package Board;

import java.util.BitSet;

/*
*   https://github.com/mafm/HashLife
*   Code in .Tree
*/

public class Board {
    private final BitSet[][] board;
    private final int dim;

    public Board(int dimension) {
        dim = dimension;
        // Create D-BitSet array
        board = new BitSet[dimension][dimension];
        // Initialized all as 0
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = new BitSet(2);
            }
        }
    }

    // Exports board as 2D-int array.
    // 0 || 1 = cell is dead
    // 2 = alive, player 1
    // 3 = alive, player 2
    public int[][] getBoard() {
        // Creating a 2D-int-array for display
        int[][] res = new int[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                res[i][j] = toInt(board[i][j]);
            }
        }
        return res;
    }

    public static int toInt(BitSet bitSet) {
        // Method to translate BitSet into int
        int intValue = 0;
        for (int bit = 0; bit < bitSet.length(); bit++) {
            if (bitSet.get(bit)) {
                intValue |= (1 << bit);
            }
        }
        return intValue;
    }

    public void setCell(int x_cor, int y_cor, boolean alive, boolean player) {
        // Set first bit status to alive or dead
        board[x_cor][y_cor].set(0, alive);
        // Set second bit to relevant player
        board[x_cor][y_cor].set(1, player);
    }

}
