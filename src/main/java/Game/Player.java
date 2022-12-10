package Game;

public class Player {

    private final String name;
    private final String color;
    private int liveCells;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.liveCells = 0;
    }

    public String getName() { return name; }

    public String getColor() { return color; }

    public int getLiveCells() { return liveCells; }

    public void addLiveCells(int cells) {
        liveCells += cells;
    }
}
