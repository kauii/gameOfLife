package GUI.Panels;

import Observer.Board.JObserver;
import Game.Player;
import Game.Singleton;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel implements JObserver {
    private final JLabel generation;
    private final JPanel mainPanel;
    private final JPanel player1Panel;
    private final JPanel player2Panel;
    private JLabel alive1;
    private  JLabel alive2;
    private JLabel placed1;
    private JLabel placed2;
    private JLabel killed1;
    private JLabel killed2;
    Singleton players = Singleton.getInstance();
    Player player1 = players.getPlayer(0);
    Player player2 = players.getPlayer(1);

    public StatisticsPanel() {
        setLayout(new BorderLayout());

        JPanel genPanel = new JPanel();
        genPanel.setBackground(new Color(200,200,200));

        generation = new JLabel("Generation: 1");
        genPanel.add(generation);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        player1Panel = createPlayerPanel(player1);
        player2Panel = createPlayerPanel(player2);

        manageLayout();

        add(genPanel, BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
    }

    private JPanel createPlayerPanel(Player player) {
        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // manage layout
        playerPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;

        // create and add general labels
        JLabel name = new JLabel(player.getName());
        JLabel cellLabel = new JLabel("Cells alive: ");

        constraints.gridy = 1;
        playerPanel.add(name, constraints);
        constraints.gridy = 2;
        playerPanel.add(new JLabel(" "), constraints);
        constraints.gridy = 3;
        playerPanel.add(cellLabel, constraints);
        constraints.gridy = 5;
        playerPanel.add(new JLabel(" "), constraints);

        // add specific labels
        if (player == players.getPlayer(0)) {
            alive1 = new JLabel("0");
            placed1 = new JLabel(" ");
            killed1 = new JLabel(" ");

            constraints.gridy = 0;
            playerPanel.add(new JLabel("Player 1:"), constraints);
            constraints.gridy = 4;
            playerPanel.add(alive1, constraints);
            constraints.gridy = 6;
            playerPanel.add(placed1, constraints);
            constraints.gridy = 7;
            playerPanel.add(killed1, constraints);
        }
        else {
            alive2 = new JLabel("0");
            placed2 = new JLabel(" ");
            killed2 = new JLabel(" ");

            constraints.gridy = 0;
            playerPanel.add(new JLabel("Player 2:"), constraints);
            constraints.gridy = 4;
            playerPanel.add(alive2, constraints);
            constraints.gridy = 6;
            playerPanel.add(placed2, constraints);
            constraints.gridy = 7;
            playerPanel.add(killed2, constraints);
        }

        return playerPanel;
    }

    private void manageLayout() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.425;
        mainPanel.add(player1Panel, constraints);
        constraints.gridy = 1;
        constraints.weighty = 0.575;
        mainPanel.add(player2Panel, constraints);
    }

    public void activePanel(Player player) {
        if (player == player1) {
            player1Panel.setBorder(BorderFactory.createLineBorder(player.getColor(), 6));
            placed1.setText("Cell placed");
            placed1.setForeground(Color.red);
            killed1.setText("Cell killed");
            killed1.setForeground(Color.red);
            resetPanel(player2);
        }
        else {
            player2Panel.setBorder(BorderFactory.createLineBorder(player.getColor(), 6));
            placed2.setText("Cell placed");
            placed2.setForeground(Color.RED);
            killed2.setText("Cell killed");
            killed2.setForeground(Color.red);
            resetPanel(player1);
        }
    }

    public void resetPanel(Player player) {
        if (player == player1) {
            player1Panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            placed1.setText(" ");
            killed1.setText(" ");
        }
        else {
            player2Panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            placed2.setText(" ");
            killed2.setText(" ");
        }
    }

    public void setGeneration(int genCounter) {
        generation.setText("Generation: " + genCounter);
    }

    public void setAlive(Player player) {
        if (player == null) {
            alive1.setText("0");
            alive2.setText("0");
        }
        else if (player == player1) {
            alive1.setText(String.valueOf(player.getLiveCells()));
        }
        else {
            alive2.setText(String.valueOf(player.getLiveCells()));
        }
    }

    @Override
    public void colorPlaced(Color color) {
        placed1.setForeground(color);
        placed2.setForeground(color);
    }

    @Override
    public void colorKilled(Color color) {
        killed1.setForeground(color);
        killed2.setForeground(color);
    }

    @Override
    public void enableStart(boolean enable) {
        // implemented in GameOfLife
    }

    @Override
    public void enableEvolve(boolean enable) {
        // implemented in GameOfLife
    }

    @Override
    public void enableUndo(boolean enable) {
        // implemented in GameOfLife
    }
}
