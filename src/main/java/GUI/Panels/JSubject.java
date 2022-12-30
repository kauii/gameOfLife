package GUI.Panels;

import GUI.Frames.JObserver;

public interface JSubject {
    void registerObserver(JObserver o);
    void notifyObserver();
}
