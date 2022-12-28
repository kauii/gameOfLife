package GUI.Panels;

import Game.GridIterator;
import Game.Observer;
import GUI.Subject;
import Game.Player;
import Game.Singleton;
import Board.PlayerNr;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.util.*;
import java.util.List;

import static Board.PlayerNr.*;

public class BoardPanel extends JPanel implements MouseListener, Subject {

    Singleton players = Singleton.getInstance();
    private final List<Observer> observers = new ArrayList<>();
    private PlayerNr[][] grid;
    private final int cellSize = 10; // size of each cell in pixels
    private final int rows;
    private final int cols; // dimensions of the grid
    private int zoom = 1; // scale factor for the cells
    private int countCells = 0;
    private boolean preRound = true;
    private Player activePlayer;
    private boolean cellPlaced = false;
    private boolean cellKilled = false;

    public BoardPanel(PlayerNr[][] grid) {
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
        Map<PlayerNr, Color> colorMap = new HashMap<>();
        colorMap.put(PLAYER1, players.getPlayer(0).getColor());
        colorMap.put(PLAYER2, players.getPlayer(1).getColor());
        colorMap.put(DEAD, Color.WHITE);

        GridIterator iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            int row = iterator.getRow();
            int col = iterator.getCol();
            PlayerNr cell = iterator.next();

            // Draw each cell
            Color color = colorMap.get(cell);
            g.setColor(color);
            g.fillRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);

            // Draw a black border around each cell
            g.setColor(Color.BLACK);
            g.drawRect(col * cellSize * zoom, row * cellSize * zoom, cellSize * zoom, cellSize * zoom);
        }
    }

    private boolean inBoard(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    private void play(int x, int y) {

        //TODO: undo in case of miss click

        if (activePlayer.getPlayerNr() == PLAYER1) {
            if (inBoard(x, y) && grid[y][x] == DEAD) {
                if (!cellPlaced) {
                    grid[y][x] = PLAYER1;
                    cellPlaced = true;
                }
            }
            else if (inBoard(x, y) && grid[y][x] == PLAYER2) {
                if (!cellKilled) {
                    grid[y][x] = DEAD;
                    cellKilled = true;
                }
            }
        }
        else {

            if (inBoard(x, y) && grid[y][x] == DEAD) {
                if (!cellPlaced) {
                    grid[y][x] = PLAYER2;
                    cellPlaced = true;
                }
            }
            else if (inBoard(x, y) && grid[y][x] == PLAYER1) {
                if (!cellKilled) {
                    grid[y][x] = DEAD;
                    cellKilled = true;
                }
            }
        }

        repaint();
        notifyObserver();
    }

    public void setGrid(PlayerNr[][] pGrid) {
        grid = pGrid;
        repaint();
    }

    private void initialCellPlacement(int x, int y) {
        if (inBoard(x, y)) {
            if (x >= 0 && x < cols && y >= 0 && y < rows && grid[y][x] == DEAD) {
                if (countCells < 6) {
                    // Create player 1s cell and the symmetrical cell for player 2
                    ++countCells;
                    grid[y][x] = PLAYER1;
                    grid[rows - 1 - y][cols - 1 - x] = PLAYER2;
                }
            }
            else if (grid[y][x] == PLAYER1 || grid[rows - 1 - y][cols - 1 - x] == PLAYER2) {
                // Erase player 1s cell and the symmetrical cell for player 2
                --countCells;
                grid[y][x] = DEAD;
                grid[rows - 1 - y][cols - 1 - x] = DEAD;
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
        for (PlayerNr[] cell : grid) {
            Arrays.fill(cell, DEAD);
        }
        // Reset attributes
        countCells = 0;
        preRound = true;
        cellPlaced = false;
        cellKilled = false;
        activePlayer = players.getPlayer(0);

        // Repaint the panel to reflect the changes
        repaint();
        notifyObserver();
    }

    public void changeActivePlayer() {
        cellPlaced = false;
        cellKilled = false;
        activePlayer = players.getPlayer(activePlayer.getPlayerNr() == PLAYER1 ? 1 : 0);
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

