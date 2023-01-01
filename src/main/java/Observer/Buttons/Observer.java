package Observer.Buttons;

public interface Observer {

    void skipGen();
    void undo();

    void clearStack();
    void reset();
}
