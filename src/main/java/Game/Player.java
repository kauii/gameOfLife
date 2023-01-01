package Game;

import java.awt.*;

public class Player {

    private final String name;
    private final Color color;
    private int liveCells;

    public Player(String name, Color color) {
        this.name = name;
        this.liveCells = 0;
        this.color=color;
    }

    public String getName() { return name; }

    public Color getColor() { return color; }

    public int getLiveCells() { return liveCells; }

    public void setLiveCells(int cells) {
        liveCells = cells;
    }
}
