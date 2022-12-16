package GUI;

import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Menu extends JFrame implements Subject {

    private final List<Observer> observers;
    Game game;

    List<Player> players = new ArrayList<>();
    String name;
    Color color;

    private final JButton start;
    private final JButton player;



    public Menu() {

        observers = new ArrayList<>();
        game = new Game(this);

        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setResizable(false);

        ImageIcon image = new ImageIcon("logo.png");
        setIconImage(image.getImage());

        JPanel panel = new JPanel();
        panel.setBackground(new Color(222,222,222));
        panel.setPreferredSize(new Dimension(100,70));

        start = new JButton("Start");
        start.setActionCommand("Start");
        start.addActionListener(new ButtonClickListener());
        start.setEnabled(false);

        player = new JButton("add Player");
        player.setActionCommand("Player");
        player.addActionListener(new ButtonClickListener());

        JButton help = new JButton("Help");
        help.setActionCommand("Help");
        help.addActionListener(new ButtonClickListener());

        JSlider setDim = new JSlider(0,1000);
        setDim.setPaintTrack(true);
        setDim.setMajorTickSpacing(250);
        setDim.setPaintLabels(true);

        panel.add(start);
        panel.add(player);
        panel.add(help);
        panel.add(setDim);

        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.white);
        panel2.setPreferredSize(new Dimension(100,190));

        JLabel player1 = new JLabel("Player slot 1");
        JLabel color1 = new JLabel("Player slot 2");

        panel2.add(player1);
        panel2.add(color1);

        add(panel, BorderLayout.NORTH);
        add(panel2, BorderLayout.SOUTH);
        setVisible(true);


    }

    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObserver() {
        for (Observer o : observers) {
            o.updatePlayers(players);
        }

    }

    private void close()  {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dispose();
    }

    private class ButtonClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            System.out.println(command);

            if (command.equals("Start")) {
                game.setUp();
                close();
            }
            if (command.equals("Player")) {
                if (players.size() <= 1) {

                    name = JOptionPane.showInputDialog(null,"Enter your name:","Player",JOptionPane.INFORMATION_MESSAGE);
                    color = JColorChooser.showDialog(null,"Choose a color for your cells",Color.BLACK);
                    players.add(new Player(name,color));
                    notifyObserver();
                    if (players.size() == 2) {
                        start.setEnabled(true);
                        player.setEnabled(false);

                    }
                }

            }
            if (command.equals("Help")) {
                JPopupMenu menu = new JPopupMenu();
                menu.setSize(500,500);
                menu.setVisible(true);
            }

        }
    }
}
