package Observer.Board;

import Board.Cell;

public interface CellObserver {
    void updateCell(int row, int col, Cell cell);
}
