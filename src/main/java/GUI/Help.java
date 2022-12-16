package GUI;

import javax.swing.*;
import java.awt.*;

public class Help extends JFrame {

    public Help() {
        setTitle("Game Description");
        setBackground(new Color(255,255,255));
        setVisible(true);
        setSize(650,200);
        setResizable(false);
        JTextArea text = new JTextArea("""
                The live cells come in two colors (one associated with each player). When a new cell
                comes to life, the cell takes on the color of the majority of its neighbors. (Since there must be three
                neighbors in order for a cell to come to life, there cannot be a tie. There must be a majority). Players
                alternate turns. On a player’s turn, he or she must kill one enemy cell and must change one empty cell to
                a cell of their own color. They are allowed to create a new cell at the location in which they
                killed an enemy cell. After a player’s turn, the Life cells go through one generation, and the play moves to
                the next player. There is always exactly one generation of evolution between separate players’ actions. The
                initial board configuration should be decided beforehand and be symmetric. A player is eliminated when
                they have no cells remaining of their color.""") {
        };
        JPanel panel = new JPanel();
        panel.add(text);

        add(panel, BorderLayout.CENTER);
    }

}
