package Board;

import java.util.BitSet;

public class Exporter {

    // Convert BitSet[][] to short[][]
    protected short[][] gridExport(BitSet[][] grid) {
        int dim = grid.length;
        short[][] res = new short[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                res[i][j] = toShort(grid[i][j]);
            }
        }
        return res;
    }

    // Convert BitSet to short
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
}
