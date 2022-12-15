package Game;

import java.awt.*;

public class Player {

    private final String name;
    private final Color color;
    private int liveCells;

    private PlayerNr aNr;

    public Player(String name, Color color) {
        this.name = name;
        this.liveCells = 0;
        this.color=color;
    }

    public String getName() { return name; }

    public Color getColor() { return color; }

    public PlayerNr getPlayerNr() { return aNr; }

    public int getLiveCells() { return liveCells; }

    public void setPlayerNr(PlayerNr pNr) { this.aNr = pNr; }

    public void addLiveCells(int cells) {
        liveCells += cells;
    }
}
