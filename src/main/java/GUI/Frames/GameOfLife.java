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
    private JScrollPane scrollPane;
    private JLabel generation;
    private JLabel alive1;
    private JLabel alive2;
    private int genCounter;


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
        scrollPane = new JScrollPane(board);

        // create statistics panel
        JPanel statistics = new JPanel();
        statistics.setLayout(new BorderLayout());

        // create generation panel in statistics panel
        JPanel genPanel = new JPanel();
        genPanel.setBackground(new Color(200,200,200));

        // create player panel in statistics panel
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new GridBagLayout());

        JPanel player1 = new JPanel();
        player1.setLayout(new GridBagLayout());
        player1.setBorder(BorderFactory.createLineBorder(Color.green, 6));

        JPanel player2 = new JPanel();
        player2.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagConstraints player1Constraints = new GridBagConstraints();
        GridBagConstraints player2Constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        player1Constraints.fill = GridBagConstraints.VERTICAL;
        player2Constraints.fill = GridBagConstraints.VERTICAL;

        player1Constraints.anchor = GridBagConstraints.NORTH;
        player2Constraints.anchor = GridBagConstraints.NORTH;

        player1Constraints.gridx = 0;
        player1Constraints.gridy = 0;

        player2Constraints.gridx = 0;
        player2Constraints.gridy = 0;

        constraints.weighty = 0.425;
        constraints.weightx = 1;

        constraints.gridx = 0;
        constraints.gridy = 0;


        generation = new JLabel("Generation: 1");
        genPanel.add(generation);

        player1.add(new JLabel("Player 1: "), player1Constraints);

        player1Constraints.gridy = 1;

        player1.add(new JLabel(players.getPlayer(0).getName()), player1Constraints);

        player1Constraints.gridy = 2;
        player1.add(new JLabel(" "), player1Constraints);

        player1Constraints.gridy = 3;

        player1.add(new JLabel(("Cells alive: ")), player1Constraints);

        player1Constraints.gridy = 4;

        alive1 = new JLabel("0");
        player1.add(alive1, player1Constraints);
        player1Constraints.gridy = 5;
        player1.add(new JLabel((" ")), player1Constraints);
        player1Constraints.gridy = 6;
        player1.add(new JLabel("Cell placed"), player1Constraints);
        player1Constraints.gridy = 7;
        player1.add(new JLabel("Cell killed"), player1Constraints);

        player2.add(new JLabel("Player 2: "), player2Constraints);

        player2Constraints.gridy = 1;

        player2.add(new JLabel(players.getPlayer(1).getName()), player2Constraints);

        player2Constraints.gridy = 2;
        player2.add(new JLabel((" ")), player2Constraints);

        player2Constraints.gridy = 3;
        player2.add(new JLabel("Cells alive: "), player2Constraints);

        player2Constraints.gridy = 4;

        alive2 = new JLabel("0");
        player2.add(alive2, player2Constraints);

        player2Constraints.gridy = 5;
        player2.add(new JLabel(" "), player2Constraints);
        player2Constraints.gridy = 6;
        player2.add(new JLabel("Cell placed"), player2Constraints);
        player2Constraints.gridy = 7;
        player2.add(new JLabel("Cell killed"), player2Constraints);

        playerPanel.add(player1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.575;

        playerPanel.add(player2, constraints);


        statistics.add(genPanel, BorderLayout.NORTH);
        statistics.add(playerPanel, BorderLayout.CENTER);

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
                board.clear();
                generation.setText("Generation: 1");
                start.setEnabled(false);
                evolve.setEnabled(false);

                // update live cells
                alive1.setText("0");
                alive2.setText("0");
            }
        });
        return resetButton;
    }

    private JButton createEvolve() {
        JButton evolveButton = new JButton("Evolve");
        evolveButton.setToolTipText("Place & Kill a cell to evolve");
        evolveButton.addActionListener(e -> {
            // event handling
            notifyObserver();
            evolveButton.setEnabled(false);
            board.changeActivePlayer();
            generation.setText("Generation: " + ++genCounter);

            // update live cells
            alive1.setText(String.valueOf(players.getPlayer(0).getLiveCells()));
            alive2.setText(String.valueOf(players.getPlayer(1).getLiveCells()));
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

    public void enableStartButton(boolean enable) {
        start.setEnabled(enable);
    }

    public void enableEvolveButton(boolean enable) {
        evolve.setEnabled(enable);
    }

    public void declareWinner(Player player) {
        if (player == null) {
            // tie message
            JOptionPane.showMessageDialog(this, "It's a tie!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // winner message
            JOptionPane.showMessageDialog(this,player.getName() + " won!!","Game Over",JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

