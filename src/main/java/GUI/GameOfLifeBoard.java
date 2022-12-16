package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameOfLifeBoard extends JPanel implements MouseListener {

    private short[][] grid;
    private int cellSize = 10; // size of each cell in pixels
    private int rows, cols; // dimensions of the grid

    public GameOfLifeBoard(short[][] grid) {
        this.grid = grid;
        rows = grid.length;
        cols = grid[0].length;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public void updateGrid(int x, int y) {
        if (x >= 0 && x < cols && y >= 0 && y < rows) {
            grid[y][x] = (grid[y][x] == 1) ? (short)0 : (short)1;
            repaint();
        }
    }

    // MouseListener methods
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / cellSize;
        int y = e.getY() / cellSize;
        updateGrid(x, y);
    }

    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
}
