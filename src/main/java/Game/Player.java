package Game;

import Board.Cell;

import java.awt.*;

public class Player {

    private final String name;
    private final Color color;
    private int liveCells;

    private Cell aNr;

    public Player(String name, Color color) {
        this.name = name;
        this.liveCells = 0;
        this.color=color;
    }

    public String getName() { return name; }

    public Color getColor() { return color; }

    public Cell getPlayerNr() { return aNr; }

    public int getLiveCells() { return liveCells; }

    public void setPlayerNr(Cell pNr) { this.aNr = pNr; }

    public void setLiveCells(int cells) {
        liveCells = cells;
    }
}
