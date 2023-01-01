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
import static GUI.Panels.Action.*;

public class BoardPanel extends JPanel implements MouseListener, JSubject, CellSubject {

    Singleton players = Singleton.getInstance();
    private Player activePlayer;
    private final Player player1 = players.getPlayer(0);
    private final Player player2 = players.getPlayer(1);
    private final List<JObserver> observers = new ArrayList<>();
    private final List<CellObserver> cellObservers = new ArrayList<>();
    private Cell[][] grid;
    private final List<Action> actions = new ArrayList<>();
    private final int cellSize = 10;
    private final int rows, cols;
    private int countCells = 0;
    private boolean preRound = true;

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
            g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);

            // Draw a black border around each cell
            g.setColor(Color.BLACK);
            g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
        }
    }

    private boolean inBoard(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    private void play(int x, int y) {
        if (activePlayer == player1) {
            if (inBoard(x, y) && grid[y][x] == DEAD) {
                if (!actions.contains(PLACED)) {
                    grid[y][x] = PLAYER1;
                    actions.add(PLACED);
                    notifyCellObserver(y,x,PLAYER1);
                }
            }
            else if (inBoard(x, y) && grid[y][x] == PLAYER2) {
                if (!actions.contains(KILLED)) {
                    grid[y][x] = DEAD;
                    actions.add(KILLED);
                    notifyCellObserver(y,x,DEAD);
                }
            }
            repaint();
        }
        else {

            if (inBoard(x, y) && grid[y][x] == DEAD) {
                if (!actions.contains(PLACED)) {
                    grid[y][x] = PLAYER2;
                    actions.add(PLACED);
                    notifyCellObserver(y,x,PLAYER2);
                }
            }
            else if (inBoard(x, y) && grid[y][x] == PLAYER1) {
                if (!actions.contains(KILLED)) {
                    grid[y][x] = DEAD;
                    actions.add(KILLED);
                    notifyCellObserver(y,x,DEAD);
                }
            }
            repaint();
        }
        notifyJObserver();
    }

    public void setGrid(Cell[][] pGrid) {
        grid = pGrid;
        repaint();
    }

    public Cell[][] getGrid() {
        return this.grid;
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
            notifyJObserver();
        }
    }

    private void updateUI(JObserver o, boolean placed, boolean killed) {
        Color green = new Color(0,180,0);
        if (placed) {
            o.colorPlaced(green);
        } else {
            o.colorPlaced(Color.RED);
        }
        if (killed) {
            o.colorKilled(green);
        } else {
            o.colorKilled(Color.RED);
        }
        o.enableUndo(placed || killed);
        o.enableEvolve(placed && killed);
    }

    public void startGame() {
        preRound = false;
    }

    public void undoLastAction() {
        actions.remove(actions.size() - 1);
        notifyJObserver();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (cellSize);
        int y = e.getY() / (cellSize);

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
        actions.clear();
        activePlayer = player1;

        // Repaint the panel to reflect the changes
        repaint();
        notifyJObserver();
    }

    public void changeActivePlayer() {
        actions.clear();
        notifyJObserver();
        activePlayer = (activePlayer == player1) ? player2 : player1;
    }

    @Override
    public void registerJObserver(JObserver o) {
        this.observers.add(o);
    }

    @Override
    public void notifyJObserver() {
        for (JObserver o : observers) {
            // check initial cell placement
            if (countCells == 6 && preRound) {
                o.enableStart(true);
            }
            // turn
            else {
                boolean placed = actions.contains(PLACED);
                boolean killed = actions.contains(KILLED);
                o.enableStart(false);
                updateUI(o, placed, killed);
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

