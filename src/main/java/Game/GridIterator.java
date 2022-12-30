package Game;

import Board.Cell;

import java.util.Iterator;

public class GridIterator implements Iterator<Cell> {
    private final Cell[][] grid;
    private int row;
    private int col;

    public GridIterator(Cell[][] grid) {
        this.grid = grid;
        this.row = 0;
        this.col = 0;
    }

    @Override
    public boolean hasNext() {
        return row < grid.length && col < grid[row].length;
    }

    @Override
    public Cell next() {
        Cell cell = grid[row][col];
        col++;
        if (col >= grid[row].length) {
            col = 0;
            row++;
        }
        return cell;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}