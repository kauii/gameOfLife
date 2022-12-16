package GUI;

import Board.Board;
import Game.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.text.html.HTML.Tag.INPUT;

public class GUI extends JFrame implements ActionListener, ChangeListener {

    private JButton start;
    private JButton restart;

    private boolean running = false;


    String name1;
    Color color1;

    String name2;
    Color color2;

    public GUI() {

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

        restart = new JButton("Restart");
        restart.setActionCommand("Restart");
        restart.setToolTipText("Restarts Game");
        restart.addActionListener(this);
        restart.setEnabled(false);




        buttonPanel.add(start);
        buttonPanel.add(restart);
        container.add(buttonPanel, BorderLayout.SOUTH);

    }


    public String getName(int nr) {
        if (nr == 0) {
            return name1;
        }
        return name2;
    }

    public Color getColor(int nr) {
        if (nr == 0) {
            return color1;
        }
        return color2;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            if (!running) {

                name1 = JOptionPane.showInputDialog(null, "Enter your name:", "Player 1", JOptionPane.INFORMATION_MESSAGE);
                color1 = JColorChooser.showDialog(null, "Player 1 - Choose a color for your cells:", Color.BLACK);

                name2 = JOptionPane.showInputDialog(null, "Enter your name:", "Player 2", JOptionPane.INFORMATION_MESSAGE);
                color2 = JColorChooser.showDialog(null, "Player 2 - Choose a color for your cells:", Color.BLACK);

                start.setEnabled(false);
                restart.setEnabled(true);

                running = true;
            }
        }
        if (e.getSource() == restart) {

            int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to restart?","Confirm",JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                start.setEnabled(true);
                restart.setEnabled(false);
                running = false;
            }

        }

    }



    @Override
    public void stateChanged(ChangeEvent e) {

    }

    public boolean getRunning() {
        return running;
    }


}

