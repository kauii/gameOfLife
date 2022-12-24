package GUI;

import Game.Observer;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObserver();
}
