package GUI;

import Game.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu extends JFrame implements Subject {

    private final List<Observer> observers;
    Game game;

    List<Player> players = new ArrayList<>();
    String name;
    Color color;

    private final JButton start;
    private final JButton player;
    private final JButton delete1;
    private final JButton delete2;
    private final JSlider dimSlider;
    private final JLabel dimLabel;
    private final JLabel player1;
    private final JLabel player2;
    private final JPanel slot1;
    private final JPanel slot2;



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
        start.addActionListener(new MenuHandler());
        start.setEnabled(false);

        player = new JButton("add Player");
        player.setActionCommand("Player");
        player.addActionListener(new MenuHandler());

        JButton help = new JButton("Help");
        help.setActionCommand("Help");
        help.addActionListener(new MenuHandler());

        dimSlider = new JSlider(100,1500);
        dimSlider.setPaintTrack(true);
        dimSlider.setMajorTickSpacing(50);
        dimSlider.setPaintLabels(false);
        dimSlider.setSnapToTicks(true);
        dimSlider.addChangeListener(new MenuHandler());

        dimLabel = new JLabel();
        dimLabel.setText("Board Dimension: " + dimSlider.getValue() + " x " + dimSlider.getValue());


        buttons.add(start);
        buttons.add(player);
        buttons.add(help);

        JPanel sliderPanel = new JPanel();

        sliderPanel.add(dimSlider);
        sliderPanel.add(dimLabel);

        JPanel playerSlots = new JPanel();
        playerSlots.setBackground(Color.white);
        playerSlots.setPreferredSize(new Dimension(300,180));

        slot1 = new JPanel();
        slot1.setBackground(new Color(200,200,200));
        slot1.setPreferredSize(new Dimension(135,174));
        slot1.setLayout(new BorderLayout());

        slot2 = new JPanel();
        slot2.setBackground(new Color(200,200,200));
        slot2.setPreferredSize(new Dimension(135,174));
        slot2.setLayout(new BorderLayout());

        player1 = new JLabel("EMPTY SLOT", SwingConstants.CENTER);
        player2 = new JLabel("EMPTY SLOT", SwingConstants.CENTER);

        //player1.setFont(new Font(null,Font.BOLD, 16));

        delete1 = new JButton("Delete");
        delete1.setActionCommand("Delete1");
        delete1.addActionListener(new MenuHandler());
        delete1.setEnabled(false);

        delete2 = new JButton("Delete");
        delete2.setActionCommand("Delete2");
        delete2.addActionListener(new MenuHandler());
        delete2.setEnabled(false);

        slot1.add(player1, BorderLayout.CENTER);
        slot1.add(delete1, BorderLayout.SOUTH);
        slot2.add(player2,BorderLayout.CENTER);
        slot2.add(delete2, BorderLayout.SOUTH);

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

    private class MenuHandler implements ActionListener, ChangeListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            System.out.println(command);

            if (command.equals("Start")) {
                game.setUp();
                game.initialBoardConfig(dimSlider.getValue());
                close();
            }
            if (command.equals("Player")) {
                if (players.size() <= 1) {

                    name = JOptionPane.showInputDialog(null,"Enter your name:","Player",JOptionPane.INFORMATION_MESSAGE);
                    color = JColorChooser.showDialog(null,"Choose a color for your cells",Color.BLACK);
                    if (name != null) {
                        players.add(new Player(name,color));
                        notifyObserver();

                        if (Objects.equals(player1.getText(), "EMPTY SLOT")) {
                            player1.setText(name);
                            slot1.setBackground(color);
                            delete1.setEnabled(true);
                        }
                        else if (Objects.equals(player2.getText(), "EMPTY SLOT")) {
                            player2.setText(name);
                            slot2.setBackground(color);
                            delete2.setEnabled(true);
                        }
                        if (players.size() == 2) {
                            start.setEnabled(true);
                            player.setEnabled(false);
                        }
                    }
                }

            }
            if (command.equals("Help")) {
                Help h = new Help();
            }
            if (command.equals("Delete1")) {
                players.remove(0);
                player1.setText("EMPTY SLOT");
                slot1.setBackground(new Color(200,200,200));
                delete1.setEnabled(false);
                player.setEnabled(true);
            }
            if (command.equals("Delete2")) {
                players.remove(1);
                player2.setText("EMPTY SLOT");
                slot2.setBackground(new Color(200,200,200));
                delete2.setEnabled(false);
                player.setEnabled(true);
            }

        }

        @Override
        public void stateChanged(ChangeEvent e) {
            dimLabel.setText("Board Dimension: " + dimSlider.getValue() + " x " + dimSlider.getValue());
        }
    }
}
