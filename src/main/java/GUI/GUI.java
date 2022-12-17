package GUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener, ChangeListener, Subject {

    private GameOfLifeBoard board;

    private JButton start;
    private JButton restart;
    private JButton evolve;
    private JScrollPane scrollPane;
    private JScrollBar xScrollBar;
    private JScrollBar yScrollBar;


    public GUI(short[][] board) {

        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.board = new GameOfLifeBoard(board);
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

        restart = new JButton("Restart");
        restart.setActionCommand("Restart");
        restart.setToolTipText("Restarts Game");
        restart.addActionListener(this);
        restart.setEnabled(false);

        evolve = createEvolve();

        buttonPanel.add(start);
        buttonPanel.add(restart);
        buttonPanel.add(evolve);

        scrollPane = new JScrollPane(board);
        xScrollBar = scrollPane.getHorizontalScrollBar();
        yScrollBar = scrollPane.getVerticalScrollBar();

        JPanel player1 = new JPanel();
        JPanel player2 = new JPanel();


        container.add(player1, BorderLayout.WEST);
        container.add(player2, BorderLayout.EAST);
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
}

