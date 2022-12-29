package GUI.Frames;

import Board.PlayerNr;
import GUI.Panels.BoardPanel;
import Game.Observer;
import GUI.Subject;
import Game.Player;
import Game.Singleton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends JFrame implements Subject {

    private BoardPanel board;
    private final PlayerNr[][] aGrid;
    Singleton players = Singleton.getInstance();
    private final List<Observer> observers = new ArrayList<>();
    private JButton start;
    private JButton reset;
    private JButton evolve;
    private JLabel generation;
    private JPanel player1Panel;
    private JPanel player2Panel;
    private JLabel alive1;
    private JLabel alive2;
    private JLabel placed1;
    private JLabel killed1;
    private JLabel placed2;
    private JLabel killed2;
    private int genCounter;
    private Player active;


    public GameOfLife(PlayerNr[][] grid) {

        aGrid = grid;

        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize(getContentPane());

        setSize(1024,768);
        setVisible(true);
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
        reset = createResetButton();
        evolve = createEvolve();

        // create button panel and add buttons
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.add(start);
        buttonPanel.add(reset);
        buttonPanel.add(evolve);

        // create board panel
        board = new BoardPanel(aGrid);
        JScrollPane scrollPane = new JScrollPane(board);

        // create statistics panel
        JPanel statistics = new JPanel();
        statistics.setLayout(new BorderLayout());

        // create generation panel in statistics panel
        JPanel genPanel = new JPanel();
        genPanel.setBackground(new Color(200,200,200));

        // create & add generation label
        generation = new JLabel("Generation: 1");
        genPanel.add(generation);

        // create player panel in statistics panel
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new GridBagLayout());

        // separate main player panel into two specific player panels
        player1Panel = createPlayerPanel(players.getPlayer(0));
        player2Panel = createPlayerPanel(players.getPlayer(1));

        // manage layout of player panels & add to main panel
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.425;
        playerPanel.add(player1Panel, constraints);
        constraints.gridy = 1;
        constraints.weighty = 0.575;
        playerPanel.add(player2Panel, constraints);

        // add generation and player panel to statistics panel
        statistics.add(genPanel, BorderLayout.NORTH);
        statistics.add(playerPanel, BorderLayout.CENTER);

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
    public void notifyObserver() {
        for (Observer o : observers) {
            o.skipGen();
        }
    }

    public void registerBoardObserver(Observer o) { board.registerObserver(o); }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");
        startButton.setToolTipText("Place 6 cells to start");
        startButton.addActionListener(e -> {
            // event handling
            board.startGame();
            start.setEnabled(false);

            active = players.getPlayer(0);
            activePanel(active);

            // update live cells
            alive1.setText(String.valueOf(players.getPlayer(0).getLiveCells()));
            alive2.setText(String.valueOf(players.getPlayer(1).getLiveCells()));

        });
        startButton.setEnabled(false);
        return startButton;
    }

    private JButton createResetButton() {
        JButton resetButton = new JButton("Reset");
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
        evolveButton.setToolTipText("Place & Kill a cell to evolve");
        evolveButton.addActionListener(e -> {
            // event handling
            evolveButton.setEnabled(false);
            board.changeActivePlayer();
            generation.setText("Generation: " + ++genCounter);

            active = (active == players.getPlayer(0)) ? players.getPlayer(1) : players.getPlayer(0);
            activePanel(active);

            // update live cells
            alive1.setText(String.valueOf(players.getPlayer(0).getLiveCells()));
            alive2.setText(String.valueOf(players.getPlayer(1).getLiveCells()));

            notifyObserver();
        });
        evolveButton.setEnabled(false);
        return evolveButton;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,155,155));
        return buttonPanel;
    }

    public void setBoard(PlayerNr[][] grid) {
        board.setGrid(grid);
        board.repaint();
    }

    public void colorPlaced() {
        placed1.setForeground(new Color (0,200,0));
        placed2.setForeground(new Color (0,200,0));
    }

    public void colorKilled() {
        killed1.setForeground(new Color (0,200,0));
        killed2.setForeground(new Color (0,200,0));
    }

    private JPanel createPlayerPanel(Player player) {
        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // layout manager
        playerPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;

        // create and add general labels
        JLabel name = new JLabel(player.getName());
        JLabel cellLabel = new JLabel("Cells alive: ");

        constraints.gridy = 1;
        playerPanel.add(name, constraints);
        constraints.gridy = 2;
        playerPanel.add(new JLabel(" "), constraints);
        constraints.gridy = 3;
        playerPanel.add(cellLabel, constraints);
        constraints.gridy = 5;
        playerPanel.add(new JLabel(" "), constraints);

        // add specific labels
        if (player == players.getPlayer(0)) {
            alive1 = new JLabel("0");
            placed1 = new JLabel(" ");
            killed1 = new JLabel(" ");

            constraints.gridy = 0;
            playerPanel.add(new JLabel("Player 1:"), constraints);
            constraints.gridy = 4;
            playerPanel.add(alive1, constraints);
            constraints.gridy = 6;
            playerPanel.add(placed1, constraints);
            constraints.gridy = 7;
            playerPanel.add(killed1, constraints);
        }
        else {
            alive2 = new JLabel("0");
            placed2 = new JLabel(" ");
            killed2 = new JLabel(" ");

            constraints.gridy = 0;
            playerPanel.add(new JLabel("Player 2:"), constraints);
            constraints.gridy = 4;
            playerPanel.add(alive2, constraints);
            constraints.gridy = 6;
            playerPanel.add(placed2, constraints);
            constraints.gridy = 7;
            playerPanel.add(killed2, constraints);
        }

        return playerPanel;
    }

    private void activePanel(Player player) {
        if (player == players.getPlayer(0)) {
            player1Panel.setBorder(BorderFactory.createLineBorder(player.getColor(), 6));
            placed1.setText("Cell placed");
            placed1.setForeground(Color.red);
            killed1.setText("Cell killed");
            killed1.setForeground(Color.red);
            resetPanel(players.getPlayer(1));
        }
        else {
            player2Panel.setBorder(BorderFactory.createLineBorder(player.getColor(), 6));
            placed2.setText("Cell placed");
            placed2.setForeground(Color.RED);
            killed2.setText("Cell killed");
            killed2.setForeground(Color.red);
            resetPanel(players.getPlayer(0));
        }
    }

    private void resetAll() {
        board.clear();
        generation.setText("Generation: 1");
        start.setEnabled(false);
        evolve.setEnabled(false);

        if (active == players.getPlayer(0)) {
            resetPanel(players.getPlayer(0));
        } else {
            resetPanel(players.getPlayer(1));
        }

        // update live cells
        alive1.setText("0");
        alive2.setText("0");
    }

    private void resetPanel(Player player) {
        if (player == players.getPlayer(0)) {
            player1Panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            placed1.setText(" ");
            killed1.setText(" ");
        }
        else {
            player2Panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            placed2.setText(" ");
            killed2.setText(" ");
        }
    }

    public void enableStartButton(boolean enable) {
        start.setEnabled(enable);
    }

    public void enableEvolveButton(boolean enable) {
        evolve.setEnabled(enable);
    }

    public void declareWinner(Player player) {
        // update final statistics
        generation.setText("Generation: " + ++genCounter);
        alive1.setText(String.valueOf(players.getPlayer(0).getLiveCells()));
        alive2.setText(String.valueOf(players.getPlayer(1).getLiveCells()));

        if (player == null) {
            // tie message
            JOptionPane.showMessageDialog(this, "It's a tie!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // winner message
            JOptionPane.showMessageDialog(this,player.getName() + " won!!","Game Over",JOptionPane.INFORMATION_MESSAGE);
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

