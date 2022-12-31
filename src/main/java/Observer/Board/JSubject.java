package Observer.Board;

public interface JSubject {
    void registerJObserver(JObserver o);
    void notifyJObserver();
}
