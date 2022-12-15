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
    private JTextField player1;
    private JTextField player2;


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

        JColorChooser cc = new JColorChooser();



    }

    public void initialize(Container container) {
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024,768));

        JPanel buttonPanel = new JPanel();


        start = new JButton("Start");
        start.setActionCommand("Start");
        start.setToolTipText("Starts Game");
        start.setBounds(200,100,250,100);
        start.setFocusable(false);
        start.addActionListener(this);

        player1 = new JTextField("Player 1");
        player2 = new JTextField("Player 2");

        buttonPanel.add(start);
        container.add(buttonPanel, BorderLayout.CENTER);

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

            name1 = JOptionPane.showInputDialog(null, "Enter your name:", "Player 1", JOptionPane.INFORMATION_MESSAGE);
            color1 = JColorChooser.showDialog(null, "Player 1 - Choose a color for your cells:", Color.BLACK);

            name2 = JOptionPane.showInputDialog(null, "Enter your name:", "Player 2", JOptionPane.INFORMATION_MESSAGE);
            color2 = JColorChooser.showDialog(null, "Player 2 - Choose a color for your cells:", Color.BLACK);


        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}

