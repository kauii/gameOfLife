package Game;

public interface Observer {
    void updateGrid(short[][] grid);

    void skipGen();

    void enableStart(boolean enable);

    void turnOver();

}
