package GUI.Frames;

import GUI.Panels.BoardPanel;
import Game.Observer;
import GUI.Subject;
import Game.Singleton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends JFrame implements ActionListener, ChangeListener, Subject {

    private BoardPanel board;
    private final short[][] aGrid;
    Singleton players = Singleton.getInstance();
    private final List<Observer> observers = new ArrayList<>();
    private JButton start;
    private JButton reset;
    private JButton evolve;
    private JScrollPane scrollPane;
    private JScrollBar xScrollBar;
    private JScrollBar yScrollBar;
    private JLabel generation;
    private int genCounter;


    public GameOfLife(short[][] grid) {

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(155,155,155));

        start = createStartButton();

        reset = createResetButton();

        evolve = createEvolve();

        buttonPanel.add(start);
        buttonPanel.add(reset);
        buttonPanel.add(evolve);

        board = new BoardPanel(aGrid);

        scrollPane = new JScrollPane(board);
        xScrollBar = scrollPane.getHorizontalScrollBar();
        yScrollBar = scrollPane.getVerticalScrollBar();

        JPanel statistics = new JPanel();
        statistics.setLayout(new BorderLayout());
        //sas
        JPanel genPanel = new JPanel();
        genPanel.setBackground(new Color(200,200,200));
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BorderLayout());
        JPanel player1 = new JPanel();
        player1.setBackground(Color.white);
        JPanel player2 = new JPanel();
        player2.setBackground(Color.white);

        generation = new JLabel("Generation: 1");
        genPanel.add(generation);
        player1.add(new JLabel("Player 1: "), SwingConstants.CENTER);
        player1.add(new JLabel(players.getPlayer(0).getName()));
        player1.add(new JLabel(("Cells alive:")));
        player1.add(new JLabel("0"));
        player1.setPreferredSize(new Dimension(genPanel.getWidth(),(container.getHeight() - genPanel.getHeight() - buttonPanel.getHeight() - 100)/2));
        player2.add(new JLabel("Player 2"));
        player2.add(new JLabel("Cells alive:"));
        player2.setPreferredSize(new Dimension(genPanel.getWidth(),(container.getHeight() - genPanel.getHeight() - buttonPanel.getHeight() - 100)/2));

        playerPanel.add(player1, BorderLayout.NORTH);
        playerPanel.add(player2, BorderLayout.SOUTH);


        statistics.add(genPanel, BorderLayout.NORTH);
        statistics.add(playerPanel, BorderLayout.CENTER);

        container.add(statistics, BorderLayout.EAST);
        container.add(buttonPanel, BorderLayout.SOUTH);
        container.add(scrollPane, BorderLayout.CENTER);
    }





    @Override
    public void actionPerformed(ActionEvent e) {

    }



    @Override
    public void stateChanged(ChangeEvent e) {

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
        startButton.setActionCommand("Start");
        startButton.addActionListener(e -> {
            // event handling
            board.startGame();
            start.setEnabled(false);
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
                board.clear();
                generation.setText("Generation: 1");
                start.setEnabled(false);
                evolve.setEnabled(false);
            }
        });
        return resetButton;
    }

    private JButton createEvolve() {
        JButton evolveButton = new JButton("Evolve");
        evolveButton.setActionCommand("Evolve");
        evolveButton.addActionListener(e -> {
            // event handling
            notifyObserver();
            evolveButton.setEnabled(false);
            board.checkWinner();
            generation.setText("Generation: " + ++genCounter);
        });
        evolveButton.setEnabled(false);
        return evolveButton;
    }

    public void setBoard(short[][] grid) {
        board.setGrid(grid);
        board.repaint();
    }

    public void enableStartButton(boolean enable) {
        start.setEnabled(enable);
    }

    public void enableEvolveButton(boolean enable) {
        evolve.setEnabled(enable);
    }
}

