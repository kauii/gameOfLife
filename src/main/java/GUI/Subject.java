package GUI;

import Game.Observer;

import java.awt.event.ActionEvent;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObserver(ActionEvent e);
}
