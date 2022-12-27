package Board;

import Game.PlayerNr;

public interface BoardInter {
    short[][] getBoard();

    short[][] setCell(int x_cor, int y_cor, boolean alive, PlayerNr playerNr);

    void evolve();

    int[] getPlayerCells();

    short[][] undo();

}
