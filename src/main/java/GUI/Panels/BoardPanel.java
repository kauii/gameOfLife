package GUI.Panels;

import Game.Observer;
import GUI.Subject;
import Game.Player;
import Game.PlayerNr;
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
    private int countCells = 0;
    private boolean preRound = true;
    private Player activePlayer;
    private boolean cellPlaced = false;
    private boolean cellKilled = false;

    public BoardPanel(short[][] grid) {
        this.grid = grid;
        rows = grid.length;
        cols = grid[0].length;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        addMouseListener(this);
        activePlayer = players.getPlayer(0);
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

    private boolean inBoard(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    private void play(int x, int y) {

        //TODO: undo in case of miss click

        if (activePlayer.getPlayerNr() == PlayerNr.PLAYER1) {
            if (inBoard(x, y) && grid[y][x] == 0 || grid[y][x] == 1) {
                if (!cellPlaced) {
                    grid[y][x] = (short) 2;
                    cellPlaced = true;
                }
            }
            else if (inBoard(x, y) && grid[y][x] == 3) {
                if (!cellKilled) {
                    grid[y][x] = (short) 0;
                    cellKilled = true;
                }
            }
        }
        else {

            if (inBoard(x, y) && grid[y][x] == 0 || grid[y][x] == 1) {
                if (!cellPlaced) {
                    grid[y][x] = (short) 3;
                    cellPlaced = true;
                }
            }
            else if (inBoard(x, y) && grid[y][x] == 2) {
                if (!cellKilled) {
                    grid[y][x] = (short) 1;
                    cellKilled = true;
                }
            }
        }

        repaint();
        notifyObserver();
    }

    public void setGrid(short[][] pGrid) {
        grid = pGrid;
        repaint();
    }

    private void initialCellPlacement(int x, int y) {
        if (inBoard(x, y)) {
            if (x >= 0 && x < cols && y >= 0 && y < rows && grid[y][x] == 0) {
                if (countCells < 6) {
                    // Create player 1s cell and the symmetrical cell for player 2
                    ++countCells;
                    grid[y][x] = (short) 2;
                    grid[rows - 1 - y][cols - 1 - x] = (short) 3;
                }
            }
            else if (grid[y][x] == 2 || grid[rows - 1 - y][cols - 1 - x] == 3) {
                // Erase player 1s cell and the symmetrical cell for player 2
                --countCells;
                grid[y][x] = 0;
                grid[rows - 1 - y][cols - 1 - x] = 0;
            }
            repaint();
            notifyObserver();
        }
    }

    public void startGame() {
        preRound = false;
    }

    // MouseListener methods
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (cellSize * zoom);
        int y = e.getY() / (cellSize * zoom);

        // place the first 4 cells
        if (preRound) { initialCellPlacement(x, y); }
        else { play(x,y); }
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

        // Repaint the panel to reflect the changes
        repaint();
        countCells = 0;
        preRound = true;
        notifyObserver();
    }

    public void changeActivePlayer() {
        cellPlaced = false;
        cellKilled = false;
        activePlayer = players.getPlayer(activePlayer.getPlayerNr() == PlayerNr.PLAYER1 ? 1 : 0);
    }

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers) {
            // update grid
            o.updateGrid(grid);
            // check initial cell placement
            if (countCells == 6 && preRound) {
                o.enableStart(true);
            }
            // turn
            else {
                o.enableStart(false);
                // notify if a cell is placed & a cell is killed
                if (cellPlaced && cellKilled) {
                    o.turnOver();
                }
            }
        }
    }
}

