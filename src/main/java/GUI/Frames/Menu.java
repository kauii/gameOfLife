package GUI.Frames;

import Game.*;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.Objects;

public class Menu extends JFrame {

    Singleton players = Singleton.getInstance();
    private final Game game;
    private String name;
    private Color color;
    private final JButton start, player, remove1, remove2;
    private final JSlider dimSlider;
    private final JLabel dimLabel, player1, player2;
    private final JPanel slot1, slot2;

    public Menu() {

        // create new Game
        game = new Game();
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // config for menu
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);

        setBackground(new Color(222, 222, 222));
        setLocationRelativeTo(null);
        setResizable(false);


        // set custom icon
        ImageIcon image = new ImageIcon("src/main/java/GUI/menu.png");
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
        sliderPanel.setBackground(new Color(222, 222, 222));

        // add slider to panel
        sliderPanel.add(dimSlider);
        sliderPanel.add(dimLabel);

        // create main panel for the 2 player slots
        JPanel playerSlots = new JPanel();
        playerSlots.setBackground(Color.white);
        playerSlots.setLayout(new BorderLayout());
        playerSlots.setBorder(null);

        // create panel for player slot 1
        slot1 = new JPanel();
        slot1.setBackground(new Color(200, 200, 200));
        slot1.setPreferredSize(new Dimension(142, 174));
        slot1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        slot1.setLayout(new BorderLayout());

        // create panel for player slot 2
        slot2 = new JPanel();
        slot2.setBackground(new Color(200, 200, 200));
        slot2.setPreferredSize(new Dimension(142, 174));
        slot2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        slot2.setLayout(new BorderLayout());

        // add player name and remove option to each slot
        slot1.add(player1, BorderLayout.CENTER);
        slot1.add(remove1, BorderLayout.SOUTH);
        slot2.add(player2, BorderLayout.CENTER);
        slot2.add(remove2, BorderLayout.SOUTH);

        // add player slots to main panel
        playerSlots.add(slot1, BorderLayout.WEST);
        playerSlots.add(slot2, BorderLayout.EAST);

        buttons.setBorder(null);
        playerSlots.setBorder(null);
        sliderPanel.setBorder(null);

        // add all panels to menu
        add(buttons, BorderLayout.NORTH);
        add(playerSlots, BorderLayout.SOUTH);
        add(sliderPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void close() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dispose();
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            // setup game with the given information
            game.setUp(dimSlider.getValue());
            // close the menu
            close();
        });
        startButton.setEnabled(false);
        return startButton;
    }

    private JButton createPlayerButton() {
        JButton playerButton = new JButton("add Player");
        playerButton.addActionListener(e -> {
            if (players.getList().size() <= 1) {

                name = JOptionPane.showInputDialog(null, "Enter your name:", "Player", JOptionPane.INFORMATION_MESSAGE);
                color = JColorChooser.showDialog(null, "Choose a color for your cells", Color.BLACK);

                if (name != null) {
                    // create new player
                    players.addToList(new Player(name, color));

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
                    if (players.getList().size() == 2) {
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
            JOptionPane.showMessageDialog(this,("""
                The live cells come in two colors (one associated with each player). When a new cell comes to life,
                the cell takes on the color of the majority of its neighbors. (Since there must be three neighbors
                in order for a cell to come to life, there cannot be a tie. There must be a majority). Players
                alternate turns. On a player’s turn, he or she must kill one enemy cell and must change one empty cell
                to a cell of their own color. They are allowed to create a new cell at the location in which they
                killed an enemy cell. After a player’s turn, the Life cells go through one generation, and the play
                moves to the next player. There is always exactly one generation of evolution between separate
                players’ actions. The initial board configuration should be decided beforehand and be symmetric.
                A player is eliminated when they have no cells remaining of their color."""), "Game Description",JOptionPane.INFORMATION_MESSAGE);
        });
        return helpButton;
    }

    private JSlider createDimSlider() {
        JSlider dimSlider = new JSlider(10, 500);
        dimSlider.setPaintTrack(true);
        dimSlider.setMajorTickSpacing(10);
        dimSlider.setPaintLabels(false);
        dimSlider.setSnapToTicks(true);
        dimSlider.setValue(100);
        dimSlider.addChangeListener(e -> {
            // Handle change event of the JSlider
            int value = ((dimSlider.getValue() + 5) / 10) * 10;
            dimLabel.setText("Board Dimension: " + value + " x " + value);
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
                players.removeFromList(0);
                player1.setText("EMPTY SLOT");
                slot1.setBackground(new Color(200, 200, 200));
                remove1.setEnabled(false);
                player.setEnabled(true);
                start.setEnabled(false);
            } else if (Objects.equals(command, "slot2")) {
                // Handle delete button event for player 2
                if (players.getList().size() == 1) { players.removeFromList(0); }
                else { players.removeFromList(1); }
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
