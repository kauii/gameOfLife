package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements MouseListener {

    private final short[][] grid;
    private final int cellSize = 10; // size of each cell in pixels
    private final int rows;
    private final int cols; // dimensions of the grid
    private int zoom = 1; // scale factor for the cells

    public BoardPanel(short[][] grid) {
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
                // Fill the cell with black or white depending on the value of grid[row][col]
                if (grid[row][col] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);
                }

                // Draw a black border around each cell
                g.setColor(Color.BLACK);
                g.drawRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);
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
        int x = e.getX() / (cellSize * zoom);
        int y = e.getY() / (cellSize * zoom);
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

    // Zoom in/out methods
    public void zoomIn() {
        if (zoom < 5) {
            zoom++;
            repaint();
        }
    }
    public void zoomOut() {
        if (zoom > 1) {
            zoom--;
            repaint();
        }
    }


}

