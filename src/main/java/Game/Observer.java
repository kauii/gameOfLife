package Game;

import Board.Cell;

public interface Observer {
    void updateGrid(Cell[][] grid);

    void skipGen();
    void undo();

    void clearStack();
}
