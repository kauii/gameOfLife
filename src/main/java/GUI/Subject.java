package GUI;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObserver(short[][] grid, String method);
}
