package Game;

import Board.PlayerNr;

public interface Observer {
    void updateGrid(PlayerNr[][] grid);

    void skipGen();

    void enableStart(boolean enable);

    void turnOver();

    void colorPlaced();

    void colorKilled();

}
