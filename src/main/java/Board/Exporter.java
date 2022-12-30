package Board;

import java.util.BitSet;

public class Exporter {

    private PlayerNr[][] expoGrid;

    public Exporter(){
        //expoGrid=new PlayerNr[dim][dim];
    }

    // Convert BitSet[][] to PlayerNr[][]
    protected PlayerNr[][] gridExport(BitSet[][] grid) {
        int dim = grid.length;
        PlayerNr[][] res = new PlayerNr[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                res[i][j] = toPlayerNr(grid[i][j]);
            }
        }
        return res;
    }

    // Convert BitSet to PlayerNr
    private static PlayerNr toPlayerNr(BitSet bitSet) {
        // Check if cell dead
        if (!bitSet.get(0)) {
            return PlayerNr.DEAD;
        }

        // Return PlayerNr if alive
        if (!bitSet.get(1)) {
            return PlayerNr.PLAYER1;
        } else {
            return PlayerNr.PLAYER2;
        }
    }
}
