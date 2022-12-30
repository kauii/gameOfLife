package GUI.Panels;

import GUI.JObserver;

public interface JSubject {
    void registerObserver(JObserver o);
    void notifyObserver();
}
