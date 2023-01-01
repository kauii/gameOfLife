package Observer.Board;

// This observer is only for updating the GUI

import java.awt.*;

public interface JObserver {

    void enableStart(boolean enable);

    void enableEvolve(boolean enable);

    void enableUndo(boolean enable);

    void colorPlaced(Color color);

    void colorKilled(Color color);
}
