package GUI.Panels;

import Observer.Board.*;
import Game.GridIterator;
import Game.Player;
import Game.Singleton;
import Board.Cell;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.util.*;
import java.util.List;

import static Board.Cell.*;

public class BoardPanel extends JPanel implements MouseListener, JSubject, CellSubject {

    Singleton players = Singleton.getInstance();
    private Player activePlayer;
    private Player player1 = players.getPlayer(0);
    private Player player2 = players.getPlayer(1);
    private final List<JObserver> observers = new ArrayList<>();
    private final List<CellObserver> cellObservers = new ArrayList<>();
    private Cell[][] grid;
    private final int cellSize = 10; // size of each cell in pixels
    private final int rows;
    private final int cols; // dimensions of the grid
    private int zoom = 1; // scale factor for the cells
    private int countCells = 0;
    private boolean preRound = true;
    private boolean cellPlaced = false;
    private boolean cellKilled = false;
    private boolean lastAction = true;

    public BoardPanel(Cell[][] grid) {
        this.grid = grid;
        rows = grid.length;
        cols = grid[0].length;
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        addMouseListener(this);
        activePlayer = player1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Map the enums to their colors
        Map<Cell, Color> colorMap = new HashMap<>();
        colorMap.put(PLAYER1, player1.getColor());
        colorMap.put(PLAYER2, player2.getColor());
        colorMap.put(DEAD, Color.WHITE);

        GridIterator iterator = new GridIterator(grid);
        while (iterator.hasNext()) {
            int row = iterator.getRow();
            int col = iterator.getCol();
            Cell cell = iterator.next();

            // Translate the value of grid[row][col] to corresponding color
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
        if (activePlayer == player1) {
            if (inBoard(x, y) && grid[y][x] == DEAD) {
                if (!cellPlaced) {
                    grid[y][x] = PLAYER1;
                    cellPlaced = true;
                    lastAction = true;
                    notifyCellObserver(y,x,PLAYER1);
                }
            }
            else if (inBoard(x, y) && grid[y][x] == PLAYER2) {
                if (!cellKilled) {
                    grid[y][x] = DEAD;
                    cellKilled = true;
                    lastAction = false;
                    notifyCellObserver(y,x,DEAD);
                }
            }
            repaint();
        }
        else {

            if (inBoard(x, y) && grid[y][x] == DEAD) {
                if (!cellPlaced) {
                    grid[y][x] = PLAYER2;
                    cellPlaced = true;
                    lastAction = true;
                    notifyCellObserver(y,x,PLAYER2);
                }
            }
            else if (inBoard(x, y) && grid[y][x] == PLAYER1) {
                if (!cellKilled) {
                    grid[y][x] = DEAD;
                    cellKilled = true;
                    lastAction = false;
                    notifyCellObserver(y,x,DEAD);
                }
            }
            repaint();
        }
        notifyObserver();
    }

    public void setGrid(Cell[][] pGrid) {
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
                    notifyCellObserver(y,x,PLAYER1);
                    notifyCellObserver(rows - 1 - y, cols - 1 - x, PLAYER2);
                }
            }
            else if (grid[y][x] == PLAYER1 || grid[rows - 1 - y][cols - 1 - x] == PLAYER2) {
                // Erase player 1s cell and the symmetrical cell for player 2
                --countCells;
                grid[y][x] = DEAD;
                grid[rows - 1 - y][cols - 1 - x] = DEAD;
                notifyCellObserver(y,x,DEAD);
                notifyCellObserver(rows - 1 - y, cols - 1 - x, DEAD);
            }
            repaint();
            notifyObserver();
        }
    }

    public void startGame() {
        preRound = false;
    }

    public void undoLastAction() {
        if (lastAction) {
            cellPlaced = false;
            if (cellKilled) {
                lastAction = false;
            }
        } else {
            cellKilled = false;
            if (cellPlaced) {
                lastAction = true;
            }
        }
        notifyObserver();
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

    public void clear() {
        // Reset the game board to its initial state
        for (Cell[] cell : grid) {
            Arrays.fill(cell, DEAD);
        }
        // Reset global variables
        countCells = 0;
        preRound = true;
        cellPlaced = false;
        cellKilled = false;
        activePlayer = player1;

        // Repaint the panel to reflect the changes
        repaint();
        notifyObserver();
    }

    public void changeActivePlayer() {
        cellPlaced = false;
        cellKilled = false;
        activePlayer = (activePlayer == player1) ? player2 : player1;
    }

    public Cell[][] getBoard() {
        return grid;
    }

    @Override
    public void registerObserver(JObserver o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver() {
        for (JObserver o : observers) {
            // check initial cell placement
            if (countCells == 6 && preRound) {
                o.enableStart(true);
            }
            // turn
            else {
                o.enableStart(false);
                if (cellPlaced) {
                    o.colorPlaced(new Color (0,200,0));
                    o.enableUndo(true);
                }
                if  (cellKilled) {
                    o.colorKilled(new Color (0,200,0));
                    o.enableUndo(true);
                }
                // notify if a cell is placed & a cell is killed
                if (cellPlaced && cellKilled) {
                    o.enableEvolve(true);
                }
                if (!cellPlaced && !cellKilled) {
                    o.enableUndo(false);
                }
                if (!cellPlaced) {
                    o.colorPlaced(Color.RED);
                }
                if (!cellKilled) {
                    o.colorKilled(Color.RED);
                }
            }
        }
    }

    @Override
    public void registerCellObserver(CellObserver o) {
        this.cellObservers.add(o);
    }

    @Override
    public void notifyCellObserver(int row, int col, Cell cell) {
        for (CellObserver o : cellObservers) {
            o.updateCell(row, col, cell);
        }
    }
}

