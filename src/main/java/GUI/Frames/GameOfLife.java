package GUI.Frames;

import Board.Cell;
import Observer.Board.JObserver;
import GUI.Panels.BoardPanel;
import GUI.Panels.StatisticsPanel;
import Observer.Board.CellObserver;
import Observer.Buttons.*;
import Game.Player;
import Game.Singleton;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
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
    Player player1 = players.getPlayer(0);
    Player player2 = players.getPlayer(1);
    private final List<Observer> observers = new ArrayList<>();
    private JButton start;
    private JButton reset;
    private JButton evolve;
    private JButton undo;
    private int genCounter;
    private Player active;


    public GameOfLife(Cell[][] grid) {

        aGrid = grid;

        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initUI(getContentPane());

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setResizable(true);

        ImageIcon image = new ImageIcon("logo.png");
        setIconImage(image.getImage());
    }

    public void initUI(Container container) {

        container.setLayout(new BorderLayout());
        container.setBackground(Color.white);

        // create buttons for button panel
        start = createStartButton();
        reset = createResetButton();
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
        JPanel outerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        outerPanel.add(board, c);
        JScrollPane scrollPane = new JScrollPane(outerPanel);

        // register observer
        board.registerJObserver(this);

        // create statistics panel
        statistics = new StatisticsPanel();

        board.registerJObserver(statistics);

        // add all main panels to the container
        container.add(statistics, BorderLayout.EAST);
        container.add(buttonPanel, BorderLayout.SOUTH);
        container.add(scrollPane, BorderLayout.CENTER);
    }

    private void resetAll() {
        board.clear();
        statistics.setGeneration(1);
        start.setEnabled(false);
        evolve.setEnabled(false);

        if (active == player1) {
            statistics.resetPanel(player1);
        } else {
            statistics.resetPanel(player2);
        }

        // update live cells
        statistics.setAlive(null);
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");
        startButton.setActionCommand("Start");
        startButton.setToolTipText("Place 6 cells to start");
        startButton.addActionListener(e -> {
            // event handling
            board.startGame();
            start.setEnabled(false);

            active = player1;
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
                notifyObserver(e);
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

            // switch active player
            active = (active == player1) ? player2 : player1;
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
            board.undoLastAction();
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
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver(ActionEvent e) {
        String command = e.getActionCommand();
        for (Observer o : observers) {
            if (Objects.equals(command, evolve.getActionCommand())) {
                System.out.println("Hallo?");
                o.skipGen();
            }
            if (Objects.equals(command, undo.getActionCommand())) {
                o.undo();
            }
            if (Objects.equals(command, start.getActionCommand())) {
                o.clearStack();
            }
            if (Objects.equals(command, reset.getActionCommand())) {
                o.reset();
            }
        }
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
    public void colorPlaced(Color color) {
        // implemented in StatisticsPanel
    }

    @Override
    public void colorKilled(Color color) {
        // implemented in StatisticsPanel
    }

    public void registerCellObserver(CellObserver o) {
        board.registerCellObserver(o);
    }

    public void declareWinner(Player player) {

        if (player == null) {
            // tie message
            JOptionPane.showMessageDialog(this, "It's a tie!", "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // winner message
            JOptionPane.showMessageDialog(this,player.getName() + " won!","Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        String[] options = new String[] {"New Round", "Exit"};
        int response = JOptionPane.showOptionDialog(this, "Do you want to play a new round?",
                "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
        if (response == 0) {
            resetAll();
            notifyObserver(new ActionEvent(this,0,"Reset"));
        }
        if (response == 1) {
            dispose();
            System.exit(0);
        }
    }
}

