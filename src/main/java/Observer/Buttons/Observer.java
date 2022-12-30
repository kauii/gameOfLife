package Observer.Buttons;

import Board.Cell;

public interface Observer {

    void skipGen();
    void undo();

    void clearStack();
}
