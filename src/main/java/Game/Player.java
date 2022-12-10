package Game;

public class Player {

    private final String name;
    private  String color;
    private int liveCells;

    public Player(String name) {
        this.name = name;
        this.liveCells = 0;
    }

    public String getName() { return name; }

    public String getColor() { return color; }

    public int getLiveCells() { return liveCells; }

    public void addLiveCells(int cells) {
        liveCells += cells;
    }
}
