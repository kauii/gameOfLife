package Board;

import java.util.ArrayList;

public interface BoardInter {
    short[][] getBoard();
    void setCell(int x_cor, int y_cor, boolean alive, boolean player);
    int getDimension();
    void evolve();
    ArrayList<short[][]> getHistory();
    int[] getPlayerCells();

}
