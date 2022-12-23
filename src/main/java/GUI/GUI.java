package GUI;

import Game.Player;
import Game.Singleton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame implements ActionListener, ChangeListener, Subject {

    public BoardPanel board;
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


    public GUI(short[][] grid) {

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

        start = new JButton("Start");
        start.setActionCommand("Start");
        start.setToolTipText("Starts Game");
        start.addActionListener(this);

        reset = createRestartButton();

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

    private JButton createEvolve() {
        JButton evolveButton = new JButton("Evolve");
        evolveButton.setActionCommand("Evolve");
        evolveButton.addActionListener(e -> {
            // event handling
            notifyObserver(null, "evolve");
            generation.setText("Generation: " + ++genCounter);
        });
        return evolveButton;
    }

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver(short[][] grid, String method) {
        for (Observer o : observers) {
            o.update(grid, method);
        }
    }


    private JButton createRestartButton() {
        JButton restartButton = new JButton("Reset");
        restartButton.setActionCommand("Reset");
        restartButton.addActionListener(e -> {
            // Handle restart button event
            int confirm = JOptionPane.showConfirmDialog(null,"Are you sure you want to reset the board?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (confirm == 0) {
                board.clear();
            }
        });
        return restartButton;
    }

    public void setBoard(short[][] grid) {
        board.setGrid(grid);
        board.repaint();
    }
}

