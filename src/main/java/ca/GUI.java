package ca;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class containing GUI: board + buttons
 */
public class GUI extends JPanel implements ActionListener, ChangeListener {
    private static final long serialVersionUID = 1L;
    private Timer timer;
    private Board board;
    private JButton start;
    private JButton next;
    private JTextField playerpoints1;
    private JTextField playerpoints2;
    private JButton clear;
    private JButton add;
    private JSlider pred;
    private JComboBox figures;
    private JFrame frame;
    private int iterNum = 0;
    private final int maxDelay = 500;
    private final int initDelay = 100;
    private boolean running = false;

    public GUI(JFrame jf) {
        frame = jf;
        timer = new Timer(initDelay, this);
        timer.stop();
    }

    /**
     * @param container to which GUI and board is added
     */
    public void initialize(Container container) {
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024, 768));

        JPanel buttonPanel = new JPanel();

        start = new JButton("Start");
        start.setActionCommand("Start");
        start.setToolTipText("Starts Game");
        start.addActionListener(this);

        next = new JButton("Next");
        next.setActionCommand("Next");
        next.setToolTipText("Nexts clock");
        next.addActionListener(this);

        clear = new JButton("Clear");
        clear.setActionCommand("clear");
        clear.setToolTipText("Clears the board");
        clear.addActionListener(this);

        add = new JButton("Add");
        add.setActionCommand("add");
        add.setToolTipText("Adds predefined pattern");
        add.addActionListener(this);

        figures = new JComboBox(Pattern.setPattern());
        figures.setToolTipText("Pattern choice");

        playerpoints1 = new JTextField("Player1");
        playerpoints2 = new JTextField("Player2");

        pred = new JSlider();
        pred.setMinimum(0);
        pred.setMaximum(maxDelay);
        pred.setToolTipText("Time speed");
        pred.addChangeListener(this);
        pred.setValue(maxDelay - timer.getDelay());

        buttonPanel.add(playerpoints1);
        buttonPanel.add(start);
        buttonPanel.add(next);
        buttonPanel.add(clear);
        buttonPanel.add(figures);
        buttonPanel.add(add);
        buttonPanel.add(pred);
        buttonPanel.add(playerpoints2);

        board = new Board(1024, 768 - buttonPanel.getHeight());
        container.add(board, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * handles clicking on each button
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)) {
            iterNum++;
            frame.setTitle("Game of Life (" + Integer.toString(iterNum) + " iteration)");
            board.iteration();
        } else {
            String command = e.getActionCommand();
            if (command.equals("Start")) {
                if (!running) {
                    String player1 = JOptionPane.showInputDialog(this, "Player1 Name?", null);
                    String player2 = JOptionPane.showInputDialog(this, "Player2 Name?", null);
                }
            } else {
                running = false;
                next.setText("Next");
            }
            if (command.equals("Next")) {
                if (!running) {
                    timer.start();
                    next.setText("Pause");
                } else {
                    timer.stop();
                    next.setText("Next");
                }
                running = !running;
                clear.setEnabled(true);

            } else if (command.equals("clear")) {
                iterNum = 0;
                timer.stop();
                next.setEnabled(true);
                board.clear();
                frame.setTitle("Cellular Automata Toolbox");
            } else if (command.equals("add")) {
                board.loadPattern((Pattern) figures.getSelectedItem());
            }

        }
    }

    /**
     * slider to control simulation speed
     *
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        timer.setDelay(maxDelay - pred.getValue());
    }
}
