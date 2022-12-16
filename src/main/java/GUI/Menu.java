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
    private JSlider dimension;
    private JLabel player1;
    private JLabel player2;



    public Menu() {

        observers = new ArrayList<>();
        game = new Game(this);

        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setResizable(false);

        ImageIcon image = new ImageIcon("logo.png");
        setIconImage(image.getImage());

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(222,222,222));
        buttons.setPreferredSize(new Dimension(100,35));

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

        dimension = new JSlider(0,1000);
        dimension.setPaintTrack(true);
        dimension.setMajorTickSpacing(250);
        dimension.setPaintLabels(true);
        dimension.setSnapToTicks(true);

        buttons.add(start);
        buttons.add(player);
        buttons.add(help);

        JPanel sliderPanel = new JPanel();
        sliderPanel.setBackground(Color.green);

        JPanel playerSlots = new JPanel();
        playerSlots.setBackground(Color.white);
        playerSlots.setPreferredSize(new Dimension(300,100));

        JPanel slot1 = new JPanel();
        slot1.setBackground(Color.cyan);
        slot1.setPreferredSize(new Dimension(135,100));

        JPanel slot2 = new JPanel();
        slot2.setBackground(Color.blue);
        slot2.setPreferredSize(new Dimension(135,100));

        player1 = new JLabel("EMPTY SLOT");
        player2 = new JLabel("EMPTY SLOT");

        playerSlots.add(slot1, BorderLayout.WEST);
        playerSlots.add(slot2, BorderLayout.EAST);

        add(buttons, BorderLayout.NORTH);
        add(playerSlots, BorderLayout.SOUTH);
        add(sliderPanel, BorderLayout.CENTER);
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
                game.initialBoardConfig(dimension.getValue());
                close();
            }
            if (command.equals("Player")) {
                if (players.size() <= 1) {

                    name = JOptionPane.showInputDialog(null,"Enter your name:","Player",JOptionPane.INFORMATION_MESSAGE);
                    color = JColorChooser.showDialog(null,"Choose a color for your cells",Color.BLACK);
                    if (name != null) {
                        players.add(new Player(name,color));
                    }
                    notifyObserver();
                    if (players.size() == 2) {
                        start.setEnabled(true);
                        player.setEnabled(false);
                        player2.setText(name);

                    }
                    if (players.size() == 1) {
                        player1.setText(name);
                    }
                }

            }
            if (command.equals("Help")) {
                Help h = new Help();
            }

        }
    }
}
