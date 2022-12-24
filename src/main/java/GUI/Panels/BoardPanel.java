package GUI.Panels;

import Game.Observer;
import GUI.Subject;
import Game.Singleton;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardPanel extends JPanel implements MouseListener, Subject {

    Singleton players = Singleton.getInstance();
    private final List<Observer> observers = new ArrayList<>();
    private short[][] grid;
    private final int cellSize = 10; // size of each cell in pixels
    private final int rows;
    private final int cols; // dimensions of the grid
    private int zoom = 1; // scale factor for the cells
    private int turn;

    public BoardPanel(short[][] grid) {
        this.grid = grid;
        rows = grid.length;
        cols = grid[0].length;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        addMouseListener(this);
        turn = 1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Fill the cell with black or white depending on the value of grid[row][col]
                if (grid[row][col] == 2) {
                    g.setColor(players.getPlayer(0).getColor());
                    g.fillRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);
                }
                else if (grid[row][col] == 3) {
                    g.setColor(players.getPlayer(1).getColor());
                    g.fillRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);
                }
                else {
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
        if (x >= 0 && x < cols && y >= 0 && y < rows && grid[y][x] == 0) {

            grid[y][x] = (short) (turn + 1);
            // Toggle the players turn
            turn = (turn == 1) ? 2 : 1;
            repaint();
            notifyObserver();
        }
    }

    public void setGrid(short[][] pGrid) {
        grid = pGrid;
        repaint();
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

    public void clear() {
        // Reset the game board to its initial state
        for (short[] shorts : grid) {
            Arrays.fill(shorts, (short) 0);
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                System.out.println(grid[row][col]);
            }
        }
        // Repaint the panel to reflect the changes
        repaint();
        notifyObserver();
    }

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers) {
            o.updateGrid(grid);
        }
    }
}

