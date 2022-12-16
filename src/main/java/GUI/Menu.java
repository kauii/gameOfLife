package GUI;

import Game.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu extends JFrame implements Subject {

    private final List<Observer> observers;
    private final Game game;
    private final List<Player> players = new ArrayList<>();
    private String name;
    private Color color;
    private final JButton start, player, remove1, remove2;
    private final JSlider dimSlider;
    private final JLabel dimLabel, player1, player2;
    private final JPanel slot1, slot2;

    public Menu() {

        observers = new ArrayList<>();

        // create new Game
        game = new Game(this);

        // config for menu
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setResizable(false);

        // set custom icon
        ImageIcon image = new ImageIcon("menu.png");
        setIconImage(image.getImage());

        // create top buttons
        start = createStartButton();
        player = createPlayerButton();
        JButton help = createHelpButton();

        // create slider
        dimSlider = createDimSlider();
        dimLabel = new JLabel("Board Dimension: " + dimSlider.getValue() + " x " + dimSlider.getValue());

        // create remove buttons
        remove1 = createRemoveButton("slot1");
        remove2 = createRemoveButton("slot2");

        // create player labels
        player1 = new JLabel("EMPTY SLOT", SwingConstants.CENTER);
        player2 = new JLabel("EMPTY SLOT", SwingConstants.CENTER);

        // create panel for buttons
        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(222, 222, 222));
        buttons.setPreferredSize(new Dimension(100, 35));

        // add buttons to panel
        buttons.add(start);
        buttons.add(player);
        buttons.add(help);

        // create panel for slider
        JPanel sliderPanel = new JPanel();

        // add slider to panel
        sliderPanel.add(dimSlider);
        sliderPanel.add(dimLabel);

        // create main panel for the 2 player slots
        JPanel playerSlots = new JPanel();
        playerSlots.setBackground(Color.white);
        playerSlots.setPreferredSize(new Dimension(300, 180));

        // create panel for player slot 1
        slot1 = new JPanel();
        slot1.setBackground(new Color(200, 200, 200));
        slot1.setPreferredSize(new Dimension(135, 174));
        slot1.setLayout(new BorderLayout());

        // create panel for player slot 2
        slot2 = new JPanel();
        slot2.setBackground(new Color(200, 200, 200));
        slot2.setPreferredSize(new Dimension(135, 174));
        slot2.setLayout(new BorderLayout());

        // add player name and remove option to each slot
        slot1.add(player1, BorderLayout.CENTER);
        slot1.add(remove1, BorderLayout.SOUTH);
        slot2.add(player2, BorderLayout.CENTER);
        slot2.add(remove2, BorderLayout.SOUTH);

        // add player slots to main panel
        playerSlots.add(slot1, BorderLayout.WEST);
        playerSlots.add(slot2, BorderLayout.EAST);

        // add all panels to menu
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

    private void close() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dispose();
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");
        startButton.setActionCommand("Start");
        startButton.addActionListener(e -> {
            // setup game with the given information
            game.setUp();
            game.initialBoardConfig(dimSlider.getValue());
            // close the menu
            close();
        });
        startButton.setEnabled(false);
        return startButton;
    }

    private JButton createPlayerButton() {
        JButton playerButton = new JButton("add Player");
        playerButton.setActionCommand("Player");
        playerButton.addActionListener(e -> {
            if (players.size() <= 1) {

                name = JOptionPane.showInputDialog(null, "Enter your name:", "Player", JOptionPane.INFORMATION_MESSAGE);
                color = JColorChooser.showDialog(null, "Choose a color for your cells", Color.BLACK);

                if (name != null) {
                    // create new player
                    players.add(new Player(name, color));
                    notifyObserver();

                    // if the first slot is empty
                    if (Objects.equals(player1.getText(), "EMPTY SLOT")) {
                        player1.setText(name);
                        slot1.setBackground(color);
                        remove1.setEnabled(true);
                    }
                    // if the second slot is empty
                    else if (Objects.equals(player2.getText(), "EMPTY SLOT")) {
                        player2.setText(name);
                        slot2.setBackground(color);
                        remove2.setEnabled(true);
                    }
                    // enable start button if 2 players
                    if (players.size() == 2) {
                        start.setEnabled(true);
                        player.setEnabled(false);
                    }
                }
            }
        });
        return playerButton;
    }

    private JButton createHelpButton() {
        JButton helpButton = new JButton("Help");
        helpButton.setActionCommand("Help");
        helpButton.addActionListener(e -> {
            // create new window with game description
            Help h = new Help();
        });
        return helpButton;
    }

    private JSlider createDimSlider() {
        JSlider dimSlider = new JSlider(100, 1500);
        dimSlider.setPaintTrack(true);
        dimSlider.setMajorTickSpacing(100);
        dimSlider.setPaintLabels(false);
        dimSlider.setSnapToTicks(true);
        dimSlider.addChangeListener(e -> {
            // Handle change event of the JSlider
            dimLabel.setText("Board Dimension: " + dimSlider.getValue() + " x " + dimSlider.getValue());
        });
        return dimSlider;
    }

    private JButton createRemoveButton(String slot) {
        JButton deleteButton = new JButton("Remove");
        deleteButton.setActionCommand(slot);
        deleteButton.addActionListener(e -> {
            // Handle delete button event
            String command = e.getActionCommand();
            if (Objects.equals(command, "slot1")) {
                // Handle delete button event for player 1
                players.remove(0);
                player1.setText("EMPTY SLOT");
                slot1.setBackground(new Color(200, 200, 200));
                remove1.setEnabled(false);
                player.setEnabled(true);
                start.setEnabled(false);
            } else if (Objects.equals(command, "slot2")) {
                // Handle delete button event for player 2
                if (players.size() == 1) { players.remove(0); }
                else { players.remove(1); }
                player2.setText("EMPTY SLOT");
                slot2.setBackground(new Color(200, 200, 200));
                remove2.setEnabled(false);
                player.setEnabled(true);
                start.setEnabled(false);
            }
        });
        deleteButton.setEnabled(false);
        return deleteButton;
    }
}
