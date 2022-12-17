package Board;

import java.util.BitSet;

/*
 *   Memento Design Pattern
 *   Converts the BitSet Grid into a short array for export and state saving
 */

public class Memento {
    private final Grid grid;

    protected Memento(Grid grid) {
        this.grid = grid;
    }

    // Exports board as 2D-short array.
    // 0 || 1 = cell is dead
    // 2 = alive, player 1
    // 3 = alive, player 2
    protected short[][] getArr(Object o) {
        // Call has to be made by Caretaker, otherwise return null
        if (!(o instanceof Caretaker)) {
            return null;
        }

        int dimension = grid.getDimension();
        short[][] res = new short[dimension][dimension];
        BitSet[][] set = grid.getGrid(this);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                // Each element is converted from BitSet to short
                res[i][j] = toShort(set[i][j]);
            }
        }
        return res;
    }

    // Internal method to translate BitSet into short
    private static short toShort(BitSet bitSet) {
        short res = 0;
        if (bitSet.get(0)) {
            res += 2;
        }
        if (bitSet.get(1)) {
            res++;
        }
        return res;
    }

    protected int getDim(){
        return grid.getDimension();
    }
}