package Board;

public interface BoardInter {
    PlayerNr[][] getBoard();

    PlayerNr[][] setCell(int x_cor, int y_cor, PlayerNr playerNr);

    void evolve();

    int[] getPlayerCells();

    PlayerNr[][] undo();

}
