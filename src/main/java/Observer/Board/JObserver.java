package Observer.Board;

// This observer is only for updating the GUI

public interface JObserver {

    void enableStart(boolean enable);

    void enableEvolve(boolean enable);

    void enableUndo(boolean enable);

    void colorPlaced();

    void colorKilled();
}
