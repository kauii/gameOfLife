package Game;

import Board.PlayerNr;

public interface Observer {
    void updateGrid(PlayerNr[][] grid);

    void skipGen();
    void undo();

    void clearStack();
}
