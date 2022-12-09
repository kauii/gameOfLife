package Board;

import java.util.ArrayList;

/*
 *   Part of Memento Design Pattern
 *   Saves all the states as short arrays
 *   Allows Board to look at them without seeing at the inner workings of Grid
 */

public class Caretaker {
    private final ArrayList<short[][]> states = new ArrayList<>();
    private final Memento mem;

    protected Caretaker(Grid grid) {
        mem = new Memento(grid);
    }

    // Saving state to history by adding it to the ArrayList
    protected void saveState() {
        states.add(mem.getArr(this));
    }

    // Returns the most recently saved grid
    protected short[][] getCurrent() {
        return states.get(states.size() - 1);
    }

    // Returns the entire history of the current game
    protected ArrayList<short[][]> getHistory() {
        return states;
    }
}
