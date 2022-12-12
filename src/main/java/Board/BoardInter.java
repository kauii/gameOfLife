package Board;

import Game.PlayerNr;

import java.util.ArrayList;

public interface BoardInter {
    short[][] getBoard();

    void setCell(int x_cor, int y_cor, boolean alive, PlayerNr playerNr);

    int getDimension();

    void evolve();

    ArrayList<short[][]> getHistory();

    int[] getPlayerCells();

}
