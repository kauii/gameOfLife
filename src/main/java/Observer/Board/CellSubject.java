package Observer.Board;

import Board.Cell;

public interface CellSubject {

    void registerCellObserver(CellObserver o);
    void notifyCellObserver(int row, int col, Cell cell);
}
