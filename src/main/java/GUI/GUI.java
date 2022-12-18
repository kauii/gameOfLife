package GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener, ChangeListener, Subject {

    private GameOfLifeBoard board;
    private short[][] aGrid;

    private JButton start;
    private JButton restart;
    private JButton evolve;
    private JScrollPane scrollPane;
    private JScrollBar xScrollBar;
    private JScrollBar yScrollBar;


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

        restart = createRestartButton();

        evolve = createEvolve();

        buttonPanel.add(start);
        buttonPanel.add(restart);
        buttonPanel.add(evolve);

        board = new GameOfLifeBoard(aGrid);

        scrollPane = new JScrollPane(board);
        xScrollBar = scrollPane.getHorizontalScrollBar();
        yScrollBar = scrollPane.getVerticalScrollBar();

        JPanel statistics = new JPanel();
        statistics.setLayout(new BorderLayout());
        //sas
        JPanel generation = new JPanel();
        generation.setBackground(new Color(200,200,200));
        JPanel players = new JPanel();
        players.setLayout(new BorderLayout());
        JPanel player1 = new JPanel();
        player1.setBackground(Color.white);
        JPanel player2 = new JPanel();
        player2.setBackground(Color.white);

        JLabel genCounter = new JLabel("Generation: 1");
        generation.add(genCounter);
        player1.add(new JLabel("Player 1"));
        player1.add(new JLabel(("Cells alive:")));
        player1.setPreferredSize(new Dimension(generation.getWidth(),(container.getHeight() - generation.getHeight() - buttonPanel.getHeight() - 100)/2));
        player2.add(new JLabel("Player 2"));
        player2.add(new JLabel("Cells alive:"));
        player2.setPreferredSize(new Dimension(generation.getWidth(),(container.getHeight() - generation.getHeight() - buttonPanel.getHeight() - 100)/2));

        players.add(player1, BorderLayout.NORTH);
        players.add(player2, BorderLayout.SOUTH);


        statistics.add(generation, BorderLayout.NORTH);
        statistics.add(players, BorderLayout.CENTER);

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
        });
        return evolveButton;
    }

    @Override
    public void registerObserver(Observer o) {

    }

    @Override
    public void notifyObserver() {

    }

    public void updateBoard(short[][] board) {
        this.board = new GameOfLifeBoard(board);
        this.board.repaint();
    }

    private JButton createRestartButton() {
        JButton restartButton = new JButton("Restart");
        restartButton.setActionCommand("Restart");
        restartButton.addActionListener(e -> {
            // Handle restart button event
            System.out.println("Restart");
            updateBoard(aGrid);
        });
        return restartButton;
    }
}

