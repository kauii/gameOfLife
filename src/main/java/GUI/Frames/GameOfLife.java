package GUI.Frames;

import Board.Cell;
import GUI.JObserver;
import GUI.Panels.BoardPanel;
import GUI.Panels.StatisticsPanel;
import Game.Observer;
import Game.Player;
import Game.Singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameOfLife extends JFrame implements Subject, JObserver {

    private BoardPanel board;
    private StatisticsPanel statistics;
    private final Cell[][] aGrid;
    Singleton players = Singleton.getInstance();
    private final List<Observer> observers = new ArrayList<>();
    private JButton start;
    private JButton evolve;
    private JButton undo;
    private int genCounter;
    private Player active;


    public GameOfLife(Cell[][] grid) {

        aGrid = grid;

        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize(getContentPane());

        setSize(1024,768);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(true);

        ImageIcon image = new ImageIcon("logo.png");
        setIconImage(image.getImage());
    }

    public void initialize(Container container) {

        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024,768));
        container.setBackground(Color.white);

        // create buttons for button panel
        start = createStartButton();
        JButton reset = createResetButton();
        evolve = createEvolve();
        undo = createUndoButton();

        // create button panel and add buttons
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.add(start);
        buttonPanel.add(reset);
        buttonPanel.add(evolve);
        buttonPanel.add(undo);

        // create board panel
        board = new BoardPanel(aGrid);
        JScrollPane scrollPane = new JScrollPane(board);

        // register observer
        board.registerObserver(this);

        // create statistics panel
        statistics = new StatisticsPanel();

        board.registerObserver(statistics);

        // add all main panels to the container
        container.add(statistics, BorderLayout.EAST);
        container.add(buttonPanel, BorderLayout.SOUTH);
        container.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver(ActionEvent e) {
        String command = e.getActionCommand();
        for (Observer o : observers) {
            o.updateGrid(board.getBoard());
            if (Objects.equals(command, evolve.getActionCommand())) {
                o.skipGen();
            }
            if (Objects.equals(command, undo.getActionCommand())) {
                o.undo();
            }
            if (Objects.equals(command, start.getActionCommand())) {
                o.clearStack();
            }
        }
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");
        startButton.setActionCommand("Start");
        startButton.setToolTipText("Place 6 cells to start");
        startButton.addActionListener(e -> {
            // event handling
            board.startGame();
            start.setEnabled(false);

            active = players.getPlayer(0);
            statistics.activePanel(active);

            notifyObserver(e);

            // update live cells
            players.getList().forEach(statistics::setAlive);
        });
        startButton.setEnabled(false);
        return startButton;
    }

    private JButton createResetButton() {
        JButton resetButton = new JButton("Reset");
        resetButton.setActionCommand("Reset");
        resetButton.addActionListener(e -> {
            // Handle restart button event
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to reset the board?", "Confirm",
                    JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (confirm == 0) {
                resetAll();
            }
        });
        return resetButton;
    }

    private JButton createEvolve() {
        JButton evolveButton = new JButton("Evolve");
        evolveButton.setActionCommand("Evolve");
        evolveButton.setToolTipText("Place & Kill a cell to evolve");
        evolveButton.addActionListener(e -> {
            // event handling
            evolveButton.setEnabled(false);
            board.changeActivePlayer();
            ++genCounter;
            statistics.setGeneration(genCounter);

            active = (active == players.getPlayer(0)) ? players.getPlayer(1) : players.getPlayer(0);
            statistics.activePanel(active);

            notifyObserver(e);

            // update live cells
            players.getList().forEach(statistics::setAlive);
        });
        evolveButton.setEnabled(false);
        return evolveButton;
    }

    private JButton createUndoButton() {
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            // event handling
            undoButton.setEnabled(false);
            notifyObserver(e);

        });
        undoButton.setEnabled(false);
        return undoButton;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,155,155));
        return buttonPanel;
    }

    public void setBoard(Cell[][] grid) {
        board.setGrid(grid);
        board.repaint();
    }

    @Override
    public void enableStart(boolean enable) {
        start.setEnabled(enable);
    }

    @Override
    public void enableEvolve(boolean enable) {
        evolve.setEnabled(enable);
    }

    @Override
    public void enableUndo(boolean enable) { undo.setEnabled(enable); }

    @Override
    public void colorPlaced() {

    }

    @Override
    public void colorKilled() {

    }
    private void resetAll() {
        board.clear();
        statistics.setGeneration(1);
        start.setEnabled(false);
        evolve.setEnabled(false);

        if (active == players.getPlayer(0)) {
            statistics.resetPanel(players.getPlayer(0));
        } else {
            statistics.resetPanel(players.getPlayer(1));
        }

        // update live cells
        statistics.setAlive(null);
    }
    public void enableUndoButton(boolean enable) {
        undo.setEnabled(enable);
    }

    public void declareWinner(Player player) {
        // update final statistics
        ++genCounter;
        statistics.setGeneration(genCounter);
        players.getList().forEach(statistics::setAlive);

        if (player == null) {
            // tie message
            JOptionPane.showMessageDialog(this, "It's a tie!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // winner message
            JOptionPane.showMessageDialog(this,player.getName() + " won!","Game Over",JOptionPane.INFORMATION_MESSAGE);
        }
        String[] options = new String[] {"New Round", "Exit"};
        int response = JOptionPane.showOptionDialog(this, "What do you want to do?", "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
        if (response == 0) {
            resetAll();
        }
        if (response == 1) {
            dispose();
            System.exit(0);
        }
    }
}

