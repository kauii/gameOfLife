package Board;

public interface BoardInter {
    Cell[][] getBoard();

    void setCell(int x_cor, int y_cor, Cell cell);

    void evolve();

    int[] getPlayerCells();

    Cell[][] undo();

    void clearStack();

}
