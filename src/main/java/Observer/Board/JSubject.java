package Observer.Board;

public interface JSubject {
    void registerObserver(JObserver o);
    void notifyObserver();
}
